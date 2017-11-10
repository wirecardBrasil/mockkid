package br.com.moip.mockkid.model;

import java.util.List;

public class Configuration {

    private Endpoint endpoint;
    private List<ResponseConfiguration> responses;

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public List<ResponseConfiguration> getResponses() {
        return responses;
    }

    public void setResponses(List<ResponseConfiguration> responses) {
        this.responses = responses;
    }
}
