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
        assertTrue(resolver.handles("headers.authorization"));
        assertTrue(resolver.handles("headers.name"));
    }

    @Test
    public void shouldNotHandle() {
        assertFalse(resolver.handles("body.authorization"));
        assertFalse(resolver.handles("url.page"));
    }

    @Test
    public void shouldExtractVariable() {
        Mockito.when(request.getHeader("authorization")).thenReturn("123");
        assertEquals("123", resolver.extract("headers.authorization", request));
    }

    @Test
    public void shouldReturnNullOnUnknownVariable() {
        Mockito.when(request.getHeader("authorization")).thenReturn(null);
        assertEquals(null, resolver.extract("headers.authorization", request));
    }

}
