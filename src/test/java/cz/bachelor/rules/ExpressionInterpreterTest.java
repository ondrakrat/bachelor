package cz.bachelor.rules;

import cz.bachelor.metamodel.condition.Condition;
import cz.bachelor.inspection.interpreter.ExpressionInterpreter;
import org.junit.Test;

/**
 * Tests the functionality of {@link cz.bachelor.inspection.interpreter.ExpressionInterpreter}.
 */
public class ExpressionInterpreterTest {

    @Test
    public void testInterpret() {
        ExpressionInterpreter interpreter = new ExpressionInterpreter();
        String expression = "!(A      ||!  (!D  ))||           (E)";
        String expression2 = "password.length() > 5 && password.length() < 20 || age >= 18";
        Condition condition = interpreter.interpret(expression2);
        System.out.println("ExpressionInterpreterTest#testInterpret() ended successfully.");
    }
}
