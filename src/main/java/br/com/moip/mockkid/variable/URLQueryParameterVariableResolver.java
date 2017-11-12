package br.com.moip.mockkid.variable;

import javax.servlet.http.HttpServletRequest;

public class URLQueryParameterVariableResolver implements VariableResolver {

    @Override
    public boolean handles(String variable) {
        return variable.startsWith("url.");
    }

    @Override
    public String extract(String name, HttpServletRequest request) {
        return request.getParameter(name.replace("url.", ""));
    }
}
