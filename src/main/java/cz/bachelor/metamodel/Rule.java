package cz.bachelor.metamodel;

import cz.bachelor.metamodel.condition.Condition;

import java.util.Set;

/**
 * Custom container for data from Drools rules.
 */
public class Rule {

    private String name;
    private String pckg;
    // functions
    // variables
    private Set<Condition> conditions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPckg() {
        return pckg;
    }

    public void setPckg(String pckg) {
        this.pckg = pckg;
    }

    public Set<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(Set<Condition> conditions) {
        this.conditions = conditions;
    }
}
