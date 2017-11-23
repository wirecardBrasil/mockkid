package br.com.moip.mockkid.model;

import java.util.Map;

public class Response {

    private Integer status;
    private Map<String, String> headers;
    private String body;

    public Response() {
    }

    public Response(Response response) {
        this(response.status, response.headers, response.body);
    }

    public Response(Integer status, Map<String, String> headers, String body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }

}
