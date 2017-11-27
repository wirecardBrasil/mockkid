package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.Endpoint;
import br.com.moip.mockkid.model.Method;
import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.model.VariableResolvers;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static br.com.moip.mockkid.model.ConditionalType.EQUALS;
import static br.com.moip.mockkid.model.ConditionalType.JAVASCRIPT;
import static org.junit.Assert.assertEquals;

public class VariableResolverTest {

    @InjectMocks
    private VariableResolver variableResolver;

    @Spy
    private VariableResolvers variableResolvers = new VariableResolvers() {{
        add(getMockVariableResolver());
    }};

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldResolveVariables() {
        Map<String, String> variables = variableResolver.resolve(getConfiguration(), null);
        assertEquals("expression_resolved", variables.get("expression"));
        assertEquals("var_resolved", variables.get("var"));
        assertEquals(2, variables.size());
    }

    private Configuration getConfiguration() {
        List<ResponseConfiguration> responseConfigurations = Lists.newArrayList(
            new ResponseConfiguration("c1", new Conditional(JAVASCRIPT, "${expression}"), null),
            new ResponseConfiguration("c2", new Conditional(EQUALS, "var", null), null),
            new ResponseConfiguration("c3", null, null)
        );

        return new Configuration(new Endpoint("/endpoint", Method.POST), responseConfigurations);
    }

    private br.com.moip.mockkid.variable.VariableResolver getMockVariableResolver() {
        return new br.com.moip.mockkid.variable.VariableResolver() {
            @Override
            public boolean handles(String variable) {
                return true;
            }
            @Override
            public String extract(String variable, HttpServletRequest request) {
                return variable + "_resolved";
            }
        };
    }

    @Bean
    private VariableResolvers getVariableResolvers() {
        VariableResolvers vr = new VariableResolvers();
        vr.add(getMockVariableResolver());
        return vr;
    }

}
