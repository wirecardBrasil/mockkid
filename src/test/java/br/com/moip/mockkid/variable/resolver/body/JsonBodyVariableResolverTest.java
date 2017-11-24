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

public class JsonBodyVariableResolverTest {

    @Mock
    private MockkidRequest request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldExtractSimpleVariable() throws IOException {
        configureRequestWithBody("{ \"name\":\"JOSE\" }");
        assertEquals("JOSE", JSONBodyVariableResolver.extractValueFromJson("body.name", request));
    }

    @Test
    public void shouldExtractVariable() throws IOException {
        configureRequestWithBody("{\"person\": { \"address\": { \"number\": 666 } } }");
        assertEquals("666", JSONBodyVariableResolver.extractValueFromJson("body.person.address.number", request));
    }

    @Test
    public void shouldReturnNullOnUnknownVariable() throws IOException {
        configureRequestWithBody("{\"person\": { \"address\": { \"street\": \"Av Paulista\" } } }");
        assertEquals(null, JSONBodyVariableResolver.extractValueFromJson("body.person.address.number", request));
    }

    private void configureRequestWithBody(String body) throws IOException {
        DelegatingServletInputStream stream = new DelegatingServletInputStream(new ByteArrayInputStream(body.getBytes()));
        Mockito.when(request.getSafeInputStream()).thenReturn(stream);
    }

}
