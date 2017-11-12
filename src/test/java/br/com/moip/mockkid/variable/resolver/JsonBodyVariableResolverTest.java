package br.com.moip.mockkid.variable.resolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonBodyVariableResolverTest {

    @InjectMocks
    private JsonBodyVariableResolver resolver;

    @Mock
    private HttpServletRequest request;

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
    public void shouldExtractSimpleVariable() throws IOException {
        String json = "{ \"name\":\"JOSE\" }";
        BufferedReader bufferedReader = new BufferedReader(new StringReader(json));
        Mockito.when(request.getReader()).thenReturn(bufferedReader);
        assertEquals("JOSE", resolver.extract("body.name", request));
    }

    @Test
    public void shouldExtractVariable() throws IOException {
        String json = "{\"person\": { \"address\": { \"number\": 666 } } }";
        BufferedReader bufferedReader = new BufferedReader(new StringReader(json));
        Mockito.when(request.getReader()).thenReturn(bufferedReader);
        assertEquals("666", resolver.extract("body.person.address.number", request));
    }

    @Test
    public void shouldReturnNullOnUnknownVariable() throws IOException {
        String json = "{\"person\": { \"address\": { \"street\": \"Av Paulista\" } } }";
        BufferedReader bufferedReader = new BufferedReader(new StringReader(json));
        Mockito.when(request.getReader()).thenReturn(bufferedReader);
        assertEquals(null, resolver.extract("body.person.address.number", request));
    }

}
