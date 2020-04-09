package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.model.MockkidRequest;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.DelegatingServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class JsonBodyVariableResolverTest {

    @Mock
    private MockkidRequest request;

    private JSONBodyVariableResolver resolver = new JSONBodyVariableResolver();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(request.getHeader("content-type")).thenReturn("application/json");
    }

    @Test
    public void shouldHandle() {
        assertTrue(resolver.canHandle("body.name", request));
    }

    @Test
    public void shouldNotHandleVariableName() {
        assertFalse(resolver.canHandle("url.url", request));
        assertFalse(resolver.canHandle("headers.authorization", request));
    }

    @Test
    public void shouldNotHandleContentType() {
        Mockito.when(request.getHeader("content-type")).thenReturn("text/plain");
        assertFalse(resolver.canHandle("body.name", request));
    }

    @Test
    public void shouldExtractSimpleVariable() throws IOException {
        configureRequestWithBody("{ \"name\":\"JOSE\" }");
        assertEquals("JOSE", resolver.extract("body.name", null, request));
    }

    @Test
    public void shouldExtractVariable() throws IOException {
        configureRequestWithBody("{\"person\": { \"address\": { \"number\": 666 } } }");
        assertEquals("666", resolver.extract("body.person.address.number", null, request));
    }

    @Test
    public void shouldReturnNullOnUnknownVariable() throws IOException {
        configureRequestWithBody("{\"person\": { \"address\": { \"street\": \"Av Paulista\" } } }");
        assertEquals(null, resolver.extract("body.person.address.number", null, request));
    }

    private void configureRequestWithBody(String body) throws IOException {
        DelegatingServletInputStream stream = new DelegatingServletInputStream(new ByteArrayInputStream(body.getBytes()));
        Mockito.when(request.getSafeInputStream()).thenReturn(stream);
    }

}
