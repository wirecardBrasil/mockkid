package br.com.moip.mockkid.variable.resolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class URLQueryParameterVariableResolverTest {

    @InjectMocks
    private URLQueryParameterVariableResolver resolver;

    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldHandle() {
        assertTrue(resolver.handles("url.authorization"));
        assertTrue(resolver.handles("url.name"));
    }

    @Test
    public void shouldNotHandle() {
        assertFalse(resolver.handles("body.authorization"));
        assertFalse(resolver.handles("headers.page"));
    }

    @Test
    public void shouldExtractVariable() {
        Mockito.when(request.getParameter("page")).thenReturn("5");
        assertEquals("5", resolver.extract("url.page", request));
    }

    @Test
    public void shouldReturnNullOnUnknownVariable() {
        Mockito.when(request.getParameter("page")).thenReturn(null);
        assertEquals(null, resolver.extract("url.page", request));
    }

}
