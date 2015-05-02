package cz.bachelor.metamodel.interpreter;

import cz.bachelor.metamodel.condition.Condition;
import cz.bachelor.metamodel.condition.Group;
import cz.bachelor.metamodel.condition.Pattern;

import java.util.Stack;

/**
 * Helper class that interprets expression string and creates a tree of {@link Condition} elements according to
 * operator priority and brackets.
 */
public class ExpressionInterpreter {

    /**
     * Interprets given string and builds respective {@link Condition} tree.
     *
     * @param expression
     * @return
     */
    public Condition interpret(String expression) {
        int bracketCount = 0;   // opening bracket count, used for distinguishing top level conditions
        int conditionStartPos = 0;  // starting position of current condition
        Group.Operator currentOperator = null;
        char previous = 0;  // previous char, used for validating doubled logical operators
        boolean negated = false;   // the next condition should be negated, i. e. is preceded by "!"
        Stack<ExpressionElement> stack = new Stack<>(); // stack for prefix notation parsing

        for (int i = 0; i < expression.length(); ++i) {
            // top level conditions are created in this layer, nested ones are build recursively
            switch (expression.charAt(i)) {
                case '(':
                    if (bracketCount++ == 0) {
                        conditionStartPos = i + 1;  // should be safe, Drools should validate this before compilation
                    }
                    break;
                case ')':
                    --bracketCount;
                    break;
                case '&':
                    if (bracketCount == 0 && previous == '&') {
                        stack.push(new ExpressionCondition(interpret(expression.substring(conditionStartPos, i - 1).trim()), negated));
                        negated = false;
                        if (currentOperator != null) {
                            stack.push(new ExpressionOperator(currentOperator));
                        }
                        currentOperator = Group.Operator.AND;
                        conditionStartPos = i + 1;
                    } else if (bracketCount == 0) {
                        previous = '&';
                    }
                    break;
                case '|':
                    if (bracketCount == 0 && previous == '|') {
                        stack.push(new ExpressionCondition(interpret(expression.substring(conditionStartPos, i - 1).trim()), negated));
                        negated = false;
                        if (currentOperator != null) {
                            stack.push(new ExpressionOperator(currentOperator));
                        }
                        currentOperator = Group.Operator.OR;
                        conditionStartPos = i + 1;
                    } else if (bracketCount == 0) {
                        previous = '|';
                    }
                    break;
                case ',':
                    if (bracketCount == 0) {
                        stack.push(new ExpressionCondition(interpret(expression.substring(conditionStartPos, i).trim()), negated));
                        negated = false;
                        if (currentOperator != null) {
                            stack.push(new ExpressionOperator(currentOperator));
                        }
                        currentOperator = Group.Operator.COMMA;
                        conditionStartPos = i + 1;
                    }
                    break;
                case '!':
                    negated = !negated;
                    break;
                default:
                    break;
            }
        }

        // the first opening bracket is omitted (if it was present), so the last one should be omitted as well
        String expressionSubstring = expression.substring(conditionStartPos, expression.endsWith(")") ? expression.length() - 1 : expression.length()).trim();
        // is the condition simple (i. e. does not contain grouping operators)
        if (expressionSubstring.split("[&|,]").length <= 1) {
            Pattern pattern = new Pattern();
            pattern.getConstraints().add(expressionSubstring);
            stack.push(new ExpressionCondition(pattern, negated));
        } else {
            stack.push(new ExpressionCondition(interpret(expressionSubstring), negated));
        }
        if (currentOperator != null) {
            stack.push(new ExpressionOperator(currentOperator));
        }

        Condition condition = interpretStack(stack);
        return condition;
    }


    /**
     * Parses the content of the stack of prefix notation conditions into one complex {@link Condition}
     *
     * @param stack
     * @return
     */
    private Condition interpretStack(Stack<ExpressionElement> stack) {
        Condition condition = null;
        while (!stack.empty()) {
            ExpressionElement element = stack.pop();
            if (element.getType() == ElementType.OPERATOR) {
                condition = handleOperator(stack, (ExpressionOperator) element);
            } else {
                condition = ((ExpressionCondition) element).getCondition();
            }
        }
        return condition;
    }

    /**
     * Handles an encountered operator and creates a {@link Group} out of it.
     *
     * @param stack    stack of {@link ExpressionElement}s
     * @param operator current operator popped from stack
     * @return {@link Group} representing the operator
     */
    private Group handleOperator(Stack<ExpressionElement> stack, ExpressionOperator operator) {
        ExpressionElement top = stack.pop();
        Condition right = top.getType() == ElementType.CONDITION ? ((ExpressionCondition) top).getCondition() :
                handleOperator(stack, (ExpressionOperator)top);
        top = stack.pop();
        Condition left = top.getType() == ElementType.CONDITION ? ((ExpressionCondition) top).getCondition() :
                handleOperator(stack, (ExpressionOperator)top);
        return new Group(left, right, operator.getOperator());
    }

    /**
     * Prints the content of the stack on standard output. Mostly for debug purposes.
     *
     * @param stack
     */
    private void printStack(Stack<ExpressionElement> stack) {
        int depth = 0;
        System.out.println("*** STACK BEGINNING ***");
        while (!stack.empty()) {
            System.out.print(depth++ + ") ");
            ExpressionElement element = stack.pop();
            if (element.getType() == ElementType.OPERATOR) {
                System.out.println("OPERATOR");
                System.out.println("\t" + ((ExpressionOperator) element).getOperator());
                ;
            } else {
                System.out.println("CONSTRAINT:");
                for (String constraint : ((Pattern) ((ExpressionCondition) element).getCondition()).getConstraints()) {
                    System.out.println("\t" + constraint);
                }
            }
        }
        System.out.println("*** STACK END ***\n");
    }
}
