package br.com.moip.mockkid.model;

import java.util.List;

public class Configuration {

    private Endpoint endpoint;
    private List<ResponseConfiguration> responseConfigurations;

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Configuration withEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public Configuration withResponses(List<ResponseConfiguration> responses) {
        this.responseConfigurations = responses;
        return this;
    }

	public List<ResponseConfiguration> getResponseConfigurations() {
		return responseConfigurations;
	}

	public void setResponseConfigurations(List<ResponseConfiguration> responseConfigurations) {
		this.responseConfigurations = responseConfigurations;
	}
}
