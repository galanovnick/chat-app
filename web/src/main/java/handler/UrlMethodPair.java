package handler;

import controller.HttpRequestMethod;

public class UrlMethodPair {

    private final String url;
    private final HttpRequestMethod method;

    public UrlMethodPair(String url, HttpRequestMethod method) {
        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public HttpRequestMethod getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrlMethodPair that = (UrlMethodPair) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        return method == that.method;

    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }
}
