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

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
