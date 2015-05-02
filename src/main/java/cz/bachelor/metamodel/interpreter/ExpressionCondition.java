package cz.bachelor.metamodel.interpreter;

import cz.bachelor.metamodel.condition.Condition;

/**
 * {@link Condition} wrapper class for stack usage.
 */
public class ExpressionCondition implements ExpressionElement {

    private Condition condition;
    private boolean negated = false;

    public ExpressionCondition(Condition condition, boolean negated) {
        this.condition = condition;
        this.negated = negated;
    }

    @Override
    public ElementType getType() {
        return ElementType.CONDITION;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public boolean isNegated() {
        return negated;
    }

    public void setNegated(boolean negated) {
        this.negated = negated;
    }
}
