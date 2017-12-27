package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.model.MockkidRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.DelegatingServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class BodyVariableResolverTest {

    private BodyVariableResolver resolver = new BodyVariableResolver();

    @Mock
    private MockkidRequest request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldHandle() {
        assertTrue(resolver.handles("body.name"));
    }

    @Test
    public void shouldNotHandle() {
        assertFalse(resolver.handles("url.url"));
        assertFalse(resolver.handles("headers.authorization"));
    }

    @Test
    public void shouldResolveVariablesInJSON() throws IOException {
        Mockito.when(request.getHeader("content-type")).thenReturn("application/json");
        configureRequestWithBody("{ \"name\":\"JOSE\" }");
        assertEquals("JOSE", resolver.extract("body.name", null, request));
    }

    @Test
    public void shouldResolveVariablesInXML() throws IOException {
        Mockito.when(request.getHeader("content-type")).thenReturn("application/xml");
        configureRequestWithBody("<name>JOSE</name>");
        assertEquals("JOSE", resolver.extract("body.name", null, request));
    }

    @Test
    public void shouldNotResolveVariablesWithoutContentType() throws IOException {
        configureRequestWithBody("{ \"name\":\"JOSE\" }");
        assertNull(resolver.extract("body.name", null, request));
    }

    protected void configureRequestWithBody(String body) throws IOException {
        DelegatingServletInputStream stream = new DelegatingServletInputStream(new ByteArrayInputStream(body.getBytes()));
        Mockito.when(request.getSafeInputStream()).thenReturn(stream);
    }
}