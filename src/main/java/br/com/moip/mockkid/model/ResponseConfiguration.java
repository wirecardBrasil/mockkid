package br.com.moip.mockkid.model;

import java.util.List;

public class ResponseConfiguration {

    private String name;
    private Conditional conditional;
    private Response response;
    private List<Regex> regexes;

    public ResponseConfiguration() {
    }

    public ResponseConfiguration(String name, Conditional conditional, Response response) {
        this.name = name;
        this.conditional = conditional;
        this.response = response;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResponseConfiguration withName(String name) {
        this.name = name;
        return this;
    }

    public Conditional getConditional() {
        return conditional;
    }

    public void setConditional(Conditional conditional) {
        this.conditional = conditional;
    }

    public ResponseConfiguration withConditional(Conditional conditional) {
        this.conditional = conditional;
        return this;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public ResponseConfiguration withResponse(Response response) {
        this.response = response;
        return this;
    }

    public List<Regex> getRegexes() {
        return regexes;
    }

    public void setRegexes(List<Regex> regexes) {
        this.regexes = regexes;
    }

    public ResponseConfiguration withRegexes(List<Regex> regexes) {
        this.regexes = regexes;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseConfiguration{" +
                "name='" + name + '\'' +
                ", conditional=" + conditional +
                ", response=" + response +
                ", regexes=" + regexes +
                '}';
    }
}
