package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.variable.VariableResolver;
import br.com.moip.mockkid.variable.resolver.body.JSONBodyVariableResolver;
import br.com.moip.mockkid.variable.resolver.body.XMLBodyVariableResolver;

import javax.servlet.http.HttpServletRequest;

public class BodyVariableResolver implements VariableResolver {

    @Override
    public boolean handles(String variable) {
        return variable.startsWith("body.");
    }

    @Override
    public String extract(String name, ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        String header = request.getHeader("content-type");
        boolean isJson = header != null && (header.contains("application/json") || header.contains("text/json"));
        boolean isXml = header != null && (header.contains("application/xml") || header.contains("text/xml"));

        if (isJson) {
            return JSONBodyVariableResolver.extractValueFromJson(name, request);
        } else if (isXml) {
            return XMLBodyVariableResolver.extractValueFromXml(name, request);
        }

        return null;
    }

}
