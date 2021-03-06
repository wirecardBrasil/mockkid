package br.com.moip.mockkid.model;

public class Endpoint {

    private String url;
    private Method method;

    public Endpoint() {
    }

    public Endpoint(String url, Method method) {
        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Endpoint withUrl (String url) {
        this.url = url;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Endpoint withMethod (Method method) {
        this.method = method;
        return this;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "url='" + url + '\'' +
                ", method=" + method +
                '}';
    }

}
