package handler;

import controller.HttpResponseMethod;

public class UrlMethodPair {

    private final String url;
    private final String method;

    public UrlMethodPair(String url, HttpResponseMethod method) {
        this.url = url;
        this.method = method.name();
    }

    public UrlMethodPair(String url, String method) {
        this.url = url;
        this.method = method;
    }

    String getUrl() {
        return url;
    }

    String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrlMethodPair that = (UrlMethodPair) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        return method != null ? method.equals(that.method) : that.method == null;

    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }
}
