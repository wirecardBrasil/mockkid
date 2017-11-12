package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.variable.VariableResolver;

import javax.servlet.http.HttpServletRequest;

public class HeaderVariableResolver implements VariableResolver {

    @Override
    public boolean handles(String variable) {
        return variable.startsWith("headers.");
    }

    @Override
    public String extract(String name, HttpServletRequest request) {
        return request.getHeader(name.replace("headers.", ""));
    }
}
