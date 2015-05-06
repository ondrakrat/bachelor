package cz.bachelor.metamodel.condition;

import java.util.HashSet;
import java.util.Set;

/**
 * Custom implementation of {@link org.drools.core.rule.Pattern} condition.
 */
public class Pattern extends Condition {

    private Set<Condition> constraints = new HashSet<>();

    /**
     * Returns a group of constraints for this condition.
     *
     * @return
     */
    public Set<Condition> getConstraints() {
        return constraints;
    }

    public void setConstraints(Set<Condition> constraints) {
        this.constraints = constraints;
    }
}
