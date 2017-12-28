package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.util.MockkidRequestUtil;
import br.com.moip.mockkid.variable.VariableResolver;

import javax.servlet.http.HttpServletRequest;

public class RawBodyVariableResolver implements VariableResolver {

    @Override
    public boolean handles(String variable) {
        return variable.equals("body");
    }

    @Override
    public String extract(String variable, ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        return MockkidRequestUtil.getBody(request);
    }


}
