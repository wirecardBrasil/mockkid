package br.com.moip.mockkid.model;

public class ConfigRoot {

	private Configuration configuration;

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public ConfigRoot withConfiguration(Configuration configuration) {
		this.configuration = configuration;
		return this;
	}

	@Override
	public String toString() {
		return "ConfigRoot{" +
				"configuration=" + configuration +
				'}';
	}

}
