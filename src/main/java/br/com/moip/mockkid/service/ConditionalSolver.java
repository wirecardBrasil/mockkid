package br.com.moip.mockkid.service;

import br.com.moip.mockkid.conditional.ConditionalExpression;
import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.ResponseConfiguration;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ConditionalSolver {

    private static final Logger logger = LoggerFactory.getLogger(ConditionalSolver.class);

    @Autowired
    private VariableResolver variableResolver;

    public ResponseConfiguration solve(Configuration configuration, HttpServletRequest request) {
        Map<String, String> variables = variableResolver.resolveElementVariables(configuration, request);
        for (ResponseConfiguration r : configuration.getResponseConfigurations()) {
            if (r.getConditional() != null && solve(r.getConditional(), variables)) {
                return  r;
            }
        }

        List<ResponseConfiguration> defaults =
                configuration.getResponseConfigurations().stream()
                        .filter(r -> r.getConditional() == null).collect(Collectors.toList());

        return (defaults.isEmpty() ? null : defaults.get(0));
    }

    public boolean solve(Conditional conditional, Map<String, String> variables) {
        for (ConditionalExpression c : conditionals()) {
            if (c.type().equals(conditional.getType()) && c.eval(conditional, variables)) return true;
        }
        return false;
    }

    @Bean
    private List<ConditionalExpression> conditionals() {
        Reflections reflections = new Reflections("br.com.moip.mockkid");
        Set<Class<? extends ConditionalExpression>> conditionals = reflections.getSubTypesOf(ConditionalExpression.class);
        List<ConditionalExpression> instances = new ArrayList<>();

        for (Class<? extends ConditionalExpression> conditional : conditionals) {
            try {
                instances.add(conditional.newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
                logger.error("Couldn't instantiate class " + conditional.getName() + ", skipping...", e);
            }
        }

        return instances;
    }

}
