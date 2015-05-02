package cz.bachelor.rules;

import cz.bachelor.metamodel.condition.Condition;
import cz.bachelor.metamodel.interpreter.ExpressionInterpreter;
import org.junit.Test;

/**
 * Tests the functionality of {@link cz.bachelor.metamodel.interpreter.ExpressionInterpreter}.
 */
public class ExpressionInterpreterTest {

    @Test
    public void testInterpret() {
        ExpressionInterpreter interpreter = new ExpressionInterpreter();
        String expression = "age > 18 || pl > 4 && pl < 20, pl > 10";
        Condition condition = interpreter.interpret(expression);
        System.out.println("ExpressionInterpreterTest#testInterpret() ended successfully.");
    }
}
