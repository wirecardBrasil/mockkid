package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.Conditional;
import br.com.moip.mockkid.model.ConditionalType;
import br.com.moip.mockkid.model.Configuration;
import br.com.moip.mockkid.model.Response;
import br.com.moip.mockkid.model.ResponseConfiguration;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

public class ResponseMatcherTest {

    private ResponseMatcher responseMatcher = new ResponseMatcher();

    @Mock
    private HttpServletRequest httpServletRequestMock;

    public ResponseMatcherTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testContainsHeader() {
        Mockito.when(httpServletRequestMock.getHeader("authorization")).thenReturn("vavis");
        Mockito.when(httpServletRequestMock.getParameter("xpto")).thenReturn("patronus");

        Response response = responseMatcher.getResponse(buildConfiguration(), httpServletRequestMock);

        System.out.println(response.toString());
    }

    private Configuration buildConfiguration() {
        Conditional c1 = new Conditional(ConditionalType.EQUALS, "headers.authorization", "vavis");
        Response response1 = new Response(200,null, "Body do ${url.xpto}");

        ResponseConfiguration rc1 = new ResponseConfiguration();
        rc1.setConditional(c1);
        rc1.setResponse(response1);

        Configuration configuration = new Configuration();
        configuration.setResponseConfigurations(Lists.newArrayList(rc1));

        return configuration;
    }

}
