package httpclient.testunit.impl;

import controller.HttpRequestMethod;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;

public class Request {

    private final List<NameValuePair> params;
    private final String url;
    private final HttpClient client;

    public Request(List<NameValuePair> params, String url, HttpClient client) {
        this.params = params;
        this.url = url;
        this.client = client;
    }

    public Request(String url, HttpClient client) {
        this.params = new ArrayList<>();
        this.url = url;
        this.client = client;
    }

    public List<NameValuePair> getParams() {
        return params;
    }

    public String getUrl() {
        return url;
    }

    public HttpClient getClient() {
        return client;
    }
}
