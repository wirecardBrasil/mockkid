package br.com.moip.mockkid.model;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class MockkidRequest extends HttpServletRequestWrapper {

    private String body = null;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public MockkidRequest(HttpServletRequest request) {
        super(request);
    }

    /**
     * See {@link ServletRequest#getInputStream()}
     */
    public InputStream getSafeInputStream() throws IOException {
        return new ByteArrayInputStream(getBody().getBytes(StandardCharsets.UTF_8.name()));
    }

    public String getBody() throws IOException {
        if (body == null) {
            body = this.getReader().lines().collect(Collectors.joining());
        }
        return body;
    }

}
