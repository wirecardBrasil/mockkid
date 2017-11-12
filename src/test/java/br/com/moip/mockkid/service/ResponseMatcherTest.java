package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.Response;
import br.com.moip.mockkid.model.ResponseConfiguration;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;

public class ResponseMatcherTest {

    @InjectMocks
    private ResponseMatcher responseMatcher;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Mock
    private Configuration configuration;

    @Mock
    private ConditionalSolver conditionalSolver;

    @Mock
    private VariableResolver variableResolver;

    public ResponseMatcherTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetResponse() {
        String responseBody = "Hello ${body.name}! Your authorization is ${headers.authorization}!";
        ResponseConfiguration responseConfiguration =
                new ResponseConfiguration("Config", new Conditional(),
                    new Response(200, null, responseBody));

        Mockito.when(conditionalSolver.solve(configuration, httpServletRequestMock)).thenReturn(responseConfiguration);
        Mockito.when(variableResolver.resolveResponseBodyVariables(responseConfiguration, httpServletRequestMock))
                .thenReturn(of("body.name", "man", "headers.authorization", "nice"));

        Response response = responseMatcher.getResponse(configuration, httpServletRequestMock);

        assertEquals("Hello man! Your authorization is nice!", response.getBody());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrownExceptionOnNotFoundResponseConfiguration() {
        Mockito.when(conditionalSolver.solve(Mockito.any(Configuration.class),
                Mockito.any(HttpServletRequest.class))).thenReturn(null);
        responseMatcher.getResponse(configuration, httpServletRequestMock);
    }

}
