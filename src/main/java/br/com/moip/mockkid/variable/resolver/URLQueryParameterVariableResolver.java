package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.variable.VariableResolver;

import javax.servlet.http.HttpServletRequest;

public class URLQueryParameterVariableResolver implements VariableResolver {

    @Override
    public boolean canHandle(String variable, HttpServletRequest request) {
        return variable.startsWith("url.");
    }

    @Override
    public String extract(String name, ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        return request.getParameter(name.replace("url.", ""));
    }
}
