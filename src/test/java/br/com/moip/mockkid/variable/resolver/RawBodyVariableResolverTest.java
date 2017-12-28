package br.com.moip.mockkid.variable.resolver;

import br.com.moip.mockkid.model.MockkidRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class RawBodyVariableResolverTest {

    @Mock
    private MockkidRequest mockkidRequest;

    private RawBodyVariableResolver rawBodyVariableResolver;

    @Before
    public void before() {
        this.rawBodyVariableResolver = new RawBodyVariableResolver();
    }

    @Test
    public void testExtractBody() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("FULL BODY".getBytes());
        doReturn(byteArrayInputStream).when(mockkidRequest).getSafeInputStream();

        String extracted = rawBodyVariableResolver.extract(null, null, mockkidRequest);
        assertEquals("FULL BODY", extracted);
    }

}