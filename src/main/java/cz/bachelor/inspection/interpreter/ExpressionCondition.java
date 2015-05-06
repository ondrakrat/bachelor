package cz.bachelor.inspection.interpreter;

import cz.bachelor.metamodel.condition.Condition;

/**
 * {@link Condition} wrapper class for stack usage.
 */
public class ExpressionCondition implements ExpressionElement {

    private Condition condition;

    public ExpressionCondition(Condition condition, boolean negated) {
        this.condition = condition;
        condition.setNegated(condition.isNegated() ^ negated);  // XOR
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
}
