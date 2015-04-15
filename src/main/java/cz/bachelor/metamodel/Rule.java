package cz.bachelor.metamodel;

import cz.bachelor.metamodel.condition.Condition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Custom container for data from Drools rules.
 */
public class Rule {

    private String name;
    private String pckg;
    // functions
    // toDo: value data type?
    private Map<String, String> globals = new HashMap<>();
    private Set<Condition> conditions = new HashSet<>();

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

    public Map<String, String> getGlobals() {
        return globals;
    }

    public void setGlobals(Map<String, String> globals) {
        this.globals = globals;
    }
}