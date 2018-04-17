package br.com.moip.mockkid.variable.resolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;

public class HeaderVariableResolverTest {

    @InjectMocks
    private HeaderVariableResolver resolver;

    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldHandle() {
        assertTrue(resolver.handles("headers.authorization", request));
        assertTrue(resolver.handles("headers.name", request));
    }

    @Test
    public void shouldNotHandle() {
        assertFalse(resolver.handles("body.authorization", request));
        assertFalse(resolver.handles("url.page", request));
    }

    @Test
    public void shouldExtractVariable() {
        Mockito.when(request.getHeader("authorization")).thenReturn("123");
        assertEquals("123", resolver.extract("headers.authorization", null, request));
    }

    @Test
    public void shouldReturnNullOnUnknownVariable() {
        Mockito.when(request.getHeader("authorization")).thenReturn(null);
        assertEquals(null, resolver.extract("headers.authorization", null, request));
    }

}
