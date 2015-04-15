package cz.bachelor.metamodel.condition;

import cz.bachelor.metamodel.Declaration;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract implementation of one condition of a business rule. A business rule may have one or more conditions,
 * that can be connected with logical operators.
 */
public abstract class Condition {

    //toDo: implement visitor for parsing into JSON
    
    private Set<Declaration> declarations = new HashSet<>();

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
