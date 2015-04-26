package cz.bachelor.metamodel.condition;

import java.util.HashSet;
import java.util.Set;

/**
 * Custom implementation of {@link org.drools.core.rule.Pattern} condition.
 */
public class Pattern extends Condition {

    private Set<String> constraints = new HashSet<>();
    //toDo: declarations - common with the ones from Condition, or separate ones?


    /**
     * Returns a group of constraints for this condition.
     *
     * @return
     */
    public Set<String> getConstraints() {
        return constraints;
    }

    public void setConstraints(Set<String> constraints) {
        this.constraints = constraints;
    }
}
