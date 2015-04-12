package cz.bachelor.metamodel.condition;

import cz.bachelor.metamodel.declaration.Declaration;

import java.util.Set;

/**
 * Abstract implementation of one condition of a business rule. A business rule may have one or more conditions,
 * that can be connected with logical operators.
 */
public abstract class Condition {

    private String condition;
    private Set<Declaration> declarations;

    /**
     * Returns String representing the condition.
     *
     * @return
     */
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Get set of variables declared in this condition.
     *
     * @return
     */
    public Set<Declaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(Set<Declaration> declarations) {
        this.declarations = declarations;
    }
}
