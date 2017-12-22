package br.com.moip.mockkid.model;

public class Regex {

    private String expression;
    private String placeholder;

    public Regex(String expression, String placeholder) {
        this.expression = expression;
        this.placeholder = placeholder;
    }

    public Regex() {
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String toString() {
        return "Regex{" +
                "expression='" + expression + '\'' +
                ", placeholder='" + placeholder + '\'' +
                '}';
    }

}
