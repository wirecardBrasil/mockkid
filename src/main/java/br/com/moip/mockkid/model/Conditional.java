package br.com.moip.mockkid.model;

public class Conditional {
    private ConditionalType type;
    private String element;
    private String value;

    public ConditionalType getType() {
        return type;
    }

    public void setType(ConditionalType type) {
        this.type = type;
    }

    public Conditional withType(ConditionalType type) {
        this.type = type;
        return this;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Conditional withElement(String element) {
        this.element = element;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Conditional withValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "Conditional{" +
                "type=" + type +
                ", element='" + element + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
