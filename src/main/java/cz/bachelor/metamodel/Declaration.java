package cz.bachelor.metamodel;

/**
 * Represents Drools variable delaration.
 */
public class Declaration {

    private String name;
    // toDo: consider interface and implementations; will we have that many types?
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
