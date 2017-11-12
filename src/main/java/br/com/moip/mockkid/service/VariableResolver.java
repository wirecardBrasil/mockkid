package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.variable.VariableResolvers;
import org.springframework.beans.factory.annotation.Autowired;
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
public class VariableResolver {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([a-zA-Z\\.]*)\\}");

    @Autowired
    private VariableResolvers variableResolvers;

    public Map<String, String> resolveElementVariables(Configuration config, HttpServletRequest request) {
        List<String> elements =
                config.getResponseConfigurations().stream()
                        .filter(m -> m.getConditional() != null)
                        .map(m -> m.getConditional().getElement()).collect(Collectors.toList());
        return resolveVariables(elements, request);
    }

    public Map<String, String> resolveResponseBodyVariables(ResponseConfiguration config, HttpServletRequest request) {
        List<String> names = new ArrayList<>();

        Matcher matcher = VARIABLE_PATTERN.matcher(config.getResponse().getBody());
        while (matcher.find()) {
            names.add(matcher.group().replace("${", "").replace("}", ""));
        }

        return resolveVariables(names, request);
    }

    private Map<String, String> resolveVariables(List<String> variableNames, HttpServletRequest request) {
        Map<String, String> variables = new HashMap<>();

        for (String name : variableNames) {
            String value = resolveVariable(name, request);
            if (value != null) variables.put(name, value);
        }

        return variables;
    }

    private String resolveVariable(String variableName, HttpServletRequest request) {
        for (br.com.moip.mockkid.variable.VariableResolver resolver : variableResolvers) {
            if (resolver.handles(variableName)) {
                String value = resolver.extract(variableName, request);
                if (value != null) {
                    return value;
                }
            }
        }

        return null;
    }
}
