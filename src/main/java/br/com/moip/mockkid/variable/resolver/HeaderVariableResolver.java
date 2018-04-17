package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.variable.VariableResolver;

import javax.servlet.http.HttpServletRequest;

public class HeaderVariableResolver implements VariableResolver {

    @Override
    public boolean canHandle(String variable, HttpServletRequest request) {
        return variable.startsWith("headers.");
    }

    @Override
    public String extract(String name, ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        return request.getHeader(name.replace("headers.", ""));
    }
}
