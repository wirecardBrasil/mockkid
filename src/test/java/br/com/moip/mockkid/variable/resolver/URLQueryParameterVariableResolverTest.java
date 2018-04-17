package br.com.moip.mockkid.variable.resolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;

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
        assertTrue(resolver.handles("url.authorization", request));
        assertTrue(resolver.handles("url.name", request));
    }

    @Test
    public void shouldNotHandle() {
        assertFalse(resolver.handles("body.authorization", request));
        assertFalse(resolver.handles("headers.page", request));
    }

    @Test
    public void shouldExtractVariable() {
        Mockito.when(request.getParameter("page")).thenReturn("5");
        assertEquals("5", resolver.extract("url.page", null, request));
    }

    @Test
    public void shouldReturnNullOnUnknownVariable() {
        Mockito.when(request.getParameter("page")).thenReturn(null);
        assertEquals(null, resolver.extract("url.page", null, request));
    }

}
