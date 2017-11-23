package br.com.moip.mockkid.variable.resolver.body;

import br.com.moip.mockkid.model.MockkidRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.DelegatingServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class XMLBodyVariableResolverTest {

    @Mock
    private MockkidRequest request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldExtractSimpleVariable() throws IOException {
        configureRequestWithBody("<name>JOSE</name>");
        assertEquals("JOSE", XMLBodyVariableResolver.extractValueFromXml("body.name", request));
    }

    @Test
    public void shouldExtractVariable() throws IOException {
        configureRequestWithBody("<person><address><number>666</number></address></person>");
        assertEquals("666", XMLBodyVariableResolver.extractValueFromXml("body.person.address.number", request));
    }

    @Test
    public void shouldReturnNullOnUnknownVariable() throws IOException {
        configureRequestWithBody("<person><address><street>Av Paulista</street></address></person>");
        assertEquals(null, XMLBodyVariableResolver.extractValueFromXml("body.person.address.number", request));
    }

    private void configureRequestWithBody(String body) throws IOException {
        DelegatingServletInputStream stream = new DelegatingServletInputStream(new ByteArrayInputStream(body.getBytes()));
        Mockito.when(request.getSafeInputStream()).thenReturn(stream);
    }

}