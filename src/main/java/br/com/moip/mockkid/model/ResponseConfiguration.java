package br.com.moip.mockkid.model;

public class ResponseConfiguration {

    private Conditional conditional;
    private Response response;

    public Conditional getConditional() {
        return conditional;
    }

    public void setConditional(Conditional conditional) {
        this.conditional = conditional;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
