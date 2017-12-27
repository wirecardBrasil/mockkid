package br.com.moip.mockkid.variable;

import br.com.moip.mockkid.model.ResponseConfiguration;

import javax.servlet.http.HttpServletRequest;

public interface VariableResolver {

    boolean handles(String variable);

    String extract(String variable, ResponseConfiguration responseConfiguration, HttpServletRequest request);

}
