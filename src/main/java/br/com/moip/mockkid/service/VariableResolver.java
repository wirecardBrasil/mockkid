package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.model.VariableResolvers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class VariableResolver {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([a-zA-Z\\._\\-]*)\\}");
    private static final Logger logger = LoggerFactory.getLogger(VariableResolver.class);

    @Autowired
    private VariableResolvers variableResolvers;

    public Map<String, String> resolve(ResponseConfiguration responseConfiguration, Conditional conditional, HttpServletRequest request) {
        Map<String, String> resolvedVariables = new HashMap<>();
        resolvedVariables.putAll(handleConditionals(request, responseConfiguration, conditional));
        logger.info("Variables = " + resolvedVariables);
        return resolvedVariables;
    }

    private Map<String, String> handleConditionals(HttpServletRequest request, ResponseConfiguration responseConfiguration, Conditional conditional) {
        if (conditional == null) {
            return Collections.emptyMap();
        }

        if (conditional.getElement() != null) {
            return resolveConfigurationElements(responseConfiguration, request);
        }

        if (conditional.getEval() != null) {
            return resolveConfigurationEvals(responseConfiguration, request);
        }

        return Collections.emptyMap();
    }

    private Map<String, String> resolveConfigurationEvals(ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        Set<String> evalSet = getVariables(responseConfiguration.getConditional().getEval());

        return resolveVariables(evalSet, responseConfiguration, request);
    }

    private Map<String, String> resolveConfigurationElements(ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        Set<String> elementSet = Stream.of(responseConfiguration.getConditional().getElement()).collect(Collectors.toSet());

        return resolveVariables(elementSet, responseConfiguration, request);
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
        Map<String, String> resolvedBodyVars = resolveVariables(getVariables(config.getResponse().getBody()), config, request);

        return resolvedBodyVars;
    }

    private Map<String, String> resolveVariables(Set<String> variableNames, ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        Map<String, String> variables = new HashMap<>();

        for (String name : variableNames) {
            String value = resolveVariable(name, responseConfiguration, request);
            variables.put(name, value);
        }

        return variables;
    }

    private String resolveVariable(String variableName, ResponseConfiguration responseConfiguration, HttpServletRequest request) {
        for (br.com.moip.mockkid.variable.VariableResolver resolver : variableResolvers) {
            if (resolver.canHandle(variableName, request)) {
                String value = resolver.extract(variableName, responseConfiguration, request);
                if (value != null) {
                    return value;
                }
            }
        }

        return null;
    }

}
