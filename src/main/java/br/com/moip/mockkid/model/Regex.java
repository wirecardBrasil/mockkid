package br.com.moip.mockkid.model;

public class Regex {

    private String expression;

    public Regex(String expression) {
        this.expression = expression;
    }

    public Regex() {
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Regex{" +
                "expression='" + expression + '\'' +
                '}';
    }

}
