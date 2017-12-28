package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.Endpoint;
import br.com.moip.mockkid.model.Method;
import br.com.moip.mockkid.model.MockkidRequest;
import br.com.moip.mockkid.model.Regex;
import br.com.moip.mockkid.model.Response;
import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.model.VariableResolvers;
import br.com.moip.mockkid.variable.resolver.RegexVariableResolver;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static br.com.moip.mockkid.model.ConditionalType.EQUALS;
import static br.com.moip.mockkid.model.ConditionalType.JAVASCRIPT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class VariableResolverTest {

    @InjectMocks
    private VariableResolver variableResolver;

    @Spy
    private VariableResolvers variableResolvers = new VariableResolvers() {{
        add(getMockVariableResolver());
        add(new RegexVariableResolver());
    }};

    @Mock
    private MockkidRequest mockkidRequest;

    @Test
    public void shouldResolveVariables() {
        Map<String, String> variables = variableResolver.resolve(getConfiguration(), null);
        assertEquals("expression_resolved", variables.get("expression"));
        assertEquals("var_resolved", variables.get("var"));
        assertEquals("with.dot_resolved", variables.get("with.dot"));
        assertEquals("with-dash_resolved", variables.get("with-dash"));
        assertEquals(4, variables.size());
    }

    @Test
    public void testResolveRegexResponseBody() throws IOException {
        doReturn("SOCIEDADE ESPORTIVA PALMEIRAS 1914").when(mockkidRequest).getBody();

        Map<String, String> variables = variableResolver.resolveResponseBodyVariables(
                getRegexResponseConfiguration(),
                mockkidRequest
        );
        assertEquals("1914", variables.get("regex.resolve_me"));
    }

    private ResponseConfiguration getRegexResponseConfiguration() {
        Regex regex = new Regex(".*([0-9]{4}).*", "resolve_me");
        ResponseConfiguration responseConfiguration = new ResponseConfiguration().withRegexes(Lists.newArrayList(regex));

        Response response = new Response();
        response.setBody("${regex.resolve_me}");

        return responseConfiguration.withResponse(response);
    }

    private Configuration getConfiguration() {
        List<ResponseConfiguration> responseConfigurations = Lists.newArrayList(
            new ResponseConfiguration("c1", new Conditional(JAVASCRIPT, "${expression}"), null),
            new ResponseConfiguration("c2", new Conditional(EQUALS, "var", null), null),
            new ResponseConfiguration("c3", null, null),
            new ResponseConfiguration("c4", new Conditional(JAVASCRIPT, "${with.dot}"), null),
            new ResponseConfiguration("c5", new Conditional(JAVASCRIPT, "${with-dash}"), null)
        );

        return new Configuration(new Endpoint("/endpoint", Method.POST), responseConfigurations);
    }

    private br.com.moip.mockkid.variable.VariableResolver getMockVariableResolver() {
        return new br.com.moip.mockkid.variable.VariableResolver() {
            @Override
            public boolean handles(String variable) {
                return !variable.startsWith("regex.");
            }
            @Override
            public String extract(String variable, ResponseConfiguration responseConfiguration, HttpServletRequest request) {
                return variable + "_resolved";
            }
        };
    }

}
