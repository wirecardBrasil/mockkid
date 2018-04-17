package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.model.MockkidRequest;
import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.variable.VariableResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RawBodyVariableResolver implements VariableResolver {

    private static final Logger logger = LoggerFactory.getLogger(RawBodyVariableResolver.class);

    @Override
    public boolean handles(String variable, HttpServletRequest request) {
        return variable.equals("body");
    }

    @Override
    public String extract(String variable, ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        try {
            return ((MockkidRequest)request).getBody();
        } catch (IOException e) {
            logger.error("Error while getting request body for variable {}", variable, e);
            return null;
        }
    }

}
