package cz.bachelor.metamodel.condition;

/**
 * Custom implementation of {@link org.drools.core.rule.EvalCondition} condition.
 */
public class Eval extends Condition {

    // toDo: Always only one constraint?
    private String constraint;

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }
}
