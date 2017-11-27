package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.model.VariableResolvers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class VariableResolver {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([a-zA-Z\\._]*)\\}");
    private static final Logger logger = LoggerFactory.getLogger(VariableResolver.class);

    @Autowired
    private VariableResolvers variableResolvers;

    public Map<String, String> resolve(Configuration config, HttpServletRequest request) {
        Set<String> variables = new HashSet<>();

        config.getResponseConfigurations().stream()
                .filter(m -> m.getConditional() != null)
                .map(m -> m.getConditional())
                .forEach(c -> {
                    if (c.getElement() != null) variables.add(c.getElement());
                    else if (c.getEval() != null) variables.addAll(getVariables(c.getEval()));
                });

        Map<String, String> resolvedVariables = resolveVariables(variables, request);
        logger.info("Variables = " + resolvedVariables);

        return resolvedVariables;
    }

    private Set<String> getVariables(String expression) {
        Set<String> names = new HashSet<>();

        Matcher matcher = VARIABLE_PATTERN.matcher(expression);
        while (matcher.find()) {
            names.add(matcher.group().replace("${", "").replace("}", ""));
        }

        return names;
    }

    public Map<String, String> resolveResponseBodyVariables(ResponseConfiguration config, HttpServletRequest request) {
        return resolveVariables(getVariables(config.getResponse().getBody()), request);
    }

    private Map<String, String> resolveVariables(Set<String> variableNames, HttpServletRequest request) {
        Map<String, String> variables = new HashMap<>();

        for (String name : variableNames) {
            String value = resolveVariable(name, request);
            variables.put(name, value);
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
