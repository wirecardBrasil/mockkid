package br.com.moip.mockkid.provider;

import br.com.moip.mockkid.variable.VariableResolver;
import br.com.moip.mockkid.variable.VariableResolvers;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class VariableResolverProvider {

    private static final Logger logger = LoggerFactory.getLogger(VariableResolverProvider.class);

    @Bean
    public VariableResolvers getVariableResolvers() {
        return loadVariableResolverClasses();
    }

    private VariableResolvers loadVariableResolverClasses() {
        Reflections reflections = new Reflections("br.com.moip.mockkid");
        Set<Class<? extends VariableResolver>> conditionals = reflections.getSubTypesOf(VariableResolver.class);
        VariableResolvers instances = new VariableResolvers();

        for (Class<? extends VariableResolver> conditional : conditionals) {
            try {
                instances.add(conditional.newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
                logger.error("Couldn't instantiate class " + conditional.getName() + ", skipping...", e);
            }
        }

        return instances;
    }

}
