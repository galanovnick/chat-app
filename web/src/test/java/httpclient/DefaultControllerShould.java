package httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.util.UUID;

import static httpclient.HttpClientTestUtils.sendGet;
import static org.junit.Assert.assertEquals;

public class DefaultControllerShould {

    private final String baseUrl = "http://localhost:8080/";
    private HttpClient client = HttpClientBuilder.create().build();

    @Test
    public void handleGetOnMainPage() {
        HttpResponse response = sendGet(baseUrl, client);

        assertEquals("Failed on root get request.", 200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void handleGetOnInvalidUrl() {
        HttpResponse response = sendGet(baseUrl + UUID.randomUUID().toString(), client);

        assertEquals("Failed on get request to invalid url.",
                404, response.getStatusLine().getStatusCode());
    }
}
