package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.MockkidRequest;
import br.com.moip.mockkid.model.Regex;
import br.com.moip.mockkid.model.ResponseConfiguration;
import br.com.moip.mockkid.variable.resolver.RegexVariableResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class RegexVariableResolverTest {

    @Mock
    private MockkidRequest mockkidRequest;

    private RegexVariableResolver regexResolver;

    @Before
    public void before() {
        this.regexResolver = new RegexVariableResolver();
    }

    @Test
    public void testResponseConfigurationWithoutRegex() {
        String resolved = regexResolver.extract(null, new ResponseConfiguration(), null);

        assertTrue(resolved.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPattern() {
        regexResolver.extract(null, buildResponseConfiguration(null), null);
    }

    @Test(expected = PatternSyntaxException.class)
    public void testInvalidPatternSyntax() throws IOException {
        regexResolver.extract(
                null,
                buildResponseConfiguration("^[A-Z"),
                buildMockkidRequestWithBody(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingRegexGroup() throws IOException {
        regexResolver.extract(
                null,
                buildResponseConfiguration("^[A-Z]+"),
                buildMockkidRequestWithBody("A"));
    }

    @Test
    public void testResolveMatchingRequest() throws IOException {
        String resolved = regexResolver.extract(
                null,
                buildResponseConfiguration("&gt;([0-9]+)&lt;"),
                buildMockkidRequestWithBody(
                                "&lt;Order&gt;\n" +
                                "       &lt;ID&gt;1914&lt;/ID&gt;\n" +
                                "&lt;/Order&gt;")
        );

        assertEquals("1914", resolved);
    }

    @Test
    public void testResolveNonMatchingRequest() throws IOException {
        String resolved = regexResolver.extract(
                null,
                buildResponseConfiguration("&gt;([0-9]+)&lt;"),
                buildMockkidRequestWithBody("PALESTRA ITALIA"));

        assertTrue(resolved.isEmpty());
    }

    private ResponseConfiguration buildResponseConfiguration(String expression) {
        ResponseConfiguration responseConfiguration = new ResponseConfiguration();

        Regex regex = new Regex(expression);
        responseConfiguration.setRegex(regex);

        return responseConfiguration;
    }

    private MockkidRequest buildMockkidRequestWithBody(String body) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        doReturn(byteArrayInputStream).when(mockkidRequest).getSafeInputStream();

        return mockkidRequest;
    }

}