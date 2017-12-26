package br.com.moip.mockkid.service;

import br.com.moip.mockkid.model.MockkidRequest;
import br.com.moip.mockkid.model.Regex;
import br.com.moip.mockkid.model.ResponseConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class RegexResolverTest {

    @Mock
    private MockkidRequest mockkidRequest;

    private RegexResolver regexResolver;

    @Before
    public void before() {
        this.regexResolver = new RegexResolver();
    }

    @Test
    public void testResponseConfigurationWithoutRegex() {
        Map<String, String> resolvedMap = regexResolver.resolve(new ResponseConfiguration(), null);

        assertTrue(resolvedMap.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPattern() {
        regexResolver.resolve(buildResponseConfiguration(null, null), null);
    }

    @Test(expected = PatternSyntaxException.class)
    public void testInvalidPatternSyntax() throws IOException {
        regexResolver.resolve(
                buildResponseConfiguration("^[A-Z", ""),
                buildMockkidRequestWithBody(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingRegexGroup() throws IOException {
        regexResolver.resolve(
                buildResponseConfiguration("^[A-Z]+", ""),
                buildMockkidRequestWithBody("A"));
    }

    @Test
    public void testResolveMatchingRequest() throws IOException {
        Map<String, String> resolvedMap = regexResolver.resolve(
                buildResponseConfiguration("&gt;([0-9]+)&lt;", "regex.payment_id"),
                buildMockkidRequestWithBody(
                                "&lt;Order&gt;\n" +
                                "       &lt;ID&gt;1914&lt;/ID&gt;\n" +
                                "&lt;/Order&gt;")
        );

        assertEquals("1914", resolvedMap.get("regex.payment_id"));
    }

    @Test
    public void testResolveNonMatchingRequest() throws IOException {
        Map<String, String> resolvedMap = regexResolver.resolve(
                buildResponseConfiguration("&gt;([0-9]+)&lt;", "regex.payment_id"),
                buildMockkidRequestWithBody("PALESTRA ITALIA"));

        assertNull(resolvedMap.get("regex.payment_id"));
    }

    private ResponseConfiguration buildResponseConfiguration(String expression, String placeholder) {
        ResponseConfiguration responseConfiguration = new ResponseConfiguration();

        Regex regex = new Regex(expression, placeholder);
        responseConfiguration.setRegex(regex);

        return responseConfiguration;
    }

    private MockkidRequest buildMockkidRequestWithBody(String body) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        doReturn(byteArrayInputStream).when(mockkidRequest).getSafeInputStream();

        return mockkidRequest;
    }

}