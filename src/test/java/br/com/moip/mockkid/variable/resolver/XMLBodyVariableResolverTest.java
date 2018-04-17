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

public class XMLBodyVariableResolverTest {

    @Mock
    private MockkidRequest request;

    private XMLBodyVariableResolver resolver = new XMLBodyVariableResolver();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(request.getHeader("content-type")).thenReturn("application/xml");
    }

    @Test
    public void shouldHandle() {
        assertTrue(resolver.handles("body.name", request));
    }

    @Test
    public void shouldNotHandleVariableName() {
        assertFalse(resolver.handles("url.url", request));
        assertFalse(resolver.handles("headers.authorization", request));
    }

    @Test
    public void shouldNotHandleContentType() {
        Mockito.when(request.getHeader("content-type")).thenReturn("text/plain");
        assertFalse(resolver.handles("body.name", request));
    }

    @Test
    public void shouldExtractSimpleVariable() throws IOException {
        configureRequestWithBody("<name>JOSE</name>");
        assertEquals("JOSE", resolver.extract("body.name", null, request));
    }

    @Test
    public void shouldExtractVariable() throws IOException {
        configureRequestWithBody("<person><address><number>666</number></address></person>");
        assertEquals("666", resolver.extract("body.person.address.number", null, request));
    }

    @Test
    public void shouldReturnNullOnUnknownVariable() throws IOException {
        configureRequestWithBody("<person><address><street>Av Paulista</street></address></person>");
        assertEquals(null, resolver.extract("body.person.address.number", null, request));
    }

    private void configureRequestWithBody(String body) throws IOException {
        DelegatingServletInputStream stream = new DelegatingServletInputStream(new ByteArrayInputStream(body.getBytes()));
        Mockito.when(request.getSafeInputStream()).thenReturn(stream);
    }

}