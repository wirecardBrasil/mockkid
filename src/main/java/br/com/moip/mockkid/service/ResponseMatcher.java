package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.Response;
import br.com.moip.mockkid.model.ResponseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class ResponseMatcher {

    @Autowired
    private ConditionalSolver conditionalSolver;

    @Autowired
    private VariableResolver variableResolver;

    public Response getResponse(Configuration config, HttpServletRequest request) {
        ResponseConfiguration responseConfiguration = conditionalSolver.solve(config, request);
        if (responseConfiguration == null) {
            throw new IllegalStateException("Response configuration not found for request.");
        }

        return buildResponse(responseConfiguration, request);
    }

    private Response buildResponse(ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        Map<String, String> variables = variableResolver.resolveResponseBodyVariables(responseConfiguration, request);

        return replaceResponseBody(responseConfiguration, variables);
    }

    private Response replaceResponseBody(ResponseConfiguration responseConfiguration, Map<String, String> variables) {
        Response response = new Response(responseConfiguration.getResponse());
        String body = response.getBody();

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            if (entry.getValue() != null) {
                body = body.replace("${" + entry.getKey() + "}", entry.getValue());
            }
        }

        response.setBody(body);
        return response;
    }

}
