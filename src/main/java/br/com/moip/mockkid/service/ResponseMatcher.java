package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.Response;
import br.com.moip.mockkid.model.ResponseConfiguration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ResponseMatcher {

    public Response getResponse(Configuration config, HttpServletRequest request) {
        ResponseConfiguration responseConfiguration = findMatchedConditionalOrDefault(config, request);
        if (responseConfiguration == null) throw new IllegalStateException(); //FIXME

        return buildResponse(responseConfiguration, request);
    }

    private Response buildResponse(ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        Map<String, String> variables = resolveResponseBodyVariables(responseConfiguration, request);
        Response response = replaceResponseBody(responseConfiguration, variables);

        return response;
    }

    private Response replaceResponseBody(ResponseConfiguration responseConfiguration, Map<String, String> variables) {
        String body = responseConfiguration.getResponse().getBody();

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            body = body.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        responseConfiguration.getResponse().setBody(body);
        return responseConfiguration.getResponse();
    }

    private ResponseConfiguration findMatchedConditionalOrDefault(Configuration config, HttpServletRequest request) {
        Map<String, String> variables = resolveElementVariables(config, request);
        for (ResponseConfiguration r : config.getResponseConfigurations()) {
            if (r.getConditional() != null && checkConditional(r.getConditional(), variables)) {
                return  r;
            }
        }

        List<ResponseConfiguration> defaults =
                config.getResponseConfigurations().stream()
                        .filter(r -> r.getConditional() == null).collect(Collectors.toList());

        return (defaults.isEmpty() ? null : defaults.get(0));
    }

    private boolean checkConditional(Conditional conditional, Map<String, String> variables) {
        switch (conditional.getType()) {
            case CONTAINS:
                return variables.getOrDefault(conditional.getElement(), "").toLowerCase()
                        .contains(conditional.getValue().toLowerCase());
            case EQUALS:
                return variables.getOrDefault(conditional.getElement(), "").
                        equalsIgnoreCase(conditional.getValue());
            default:
                return false;
        }
    }

    private Map<String, String> resolveElementVariables(Configuration config, HttpServletRequest request) {
        List<String> elements =
                config.getResponseConfigurations().stream()
                        .filter(m -> m.getConditional() != null)
                        .map(m -> m.getConditional().getElement()).collect(Collectors.toList());
        return resolveVariables(elements, request);
    }

    private Map<String, String> resolveResponseBodyVariables(ResponseConfiguration config, HttpServletRequest request) {
        List<String> names = new ArrayList<>();

        Matcher matcher = Pattern.compile("\\$\\{([a-zA-Z\\.]*)\\}").matcher(config.getResponse().getBody());
        while (matcher.find()) {
            names.add(matcher.group().replace("${", "").replace("}", ""));
        }

        return resolveVariables(names, request);
    }

    private Map<String, String> resolveVariables(List<String> names, HttpServletRequest request) {
        Map<String, String> variables = new HashMap<>();

        names.forEach(name -> {
            if (name.startsWith("body.")) {
                //TODO parse JSON/XML
            } else if (name.startsWith("headers.")) {
                String header = request.getHeader(name.replace("headers.", ""));
                if (header != null) variables.put(name, header);
            } else if (name.startsWith("url.")) {
                String queryParam = request.getParameter(name.replace("url.", ""));
                if (queryParam != null) variables.put(name, queryParam);
            }
        });

        return variables;
    }

}
