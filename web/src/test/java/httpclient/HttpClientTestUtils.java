package httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static org.apache.http.HttpHeaders.USER_AGENT;
import static org.junit.Assert.fail;

public interface HttpClientTestUtils {

    static String getResponseContent(HttpResponse response, HttpClient client) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception");
        }
        return null;
    }

    static HttpResponse sendGet(String url, HttpClient client) {
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception.");
        }
        return response;
    }

    static HttpResponse sendPost(String url, List<NameValuePair> params, HttpClient client) {
        HttpPost request = new HttpPost(url);
        request.setHeader("User-Agent", USER_AGENT);

        try {
            request.setEntity(new UrlEncodedFormEntity(params));
            return client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception.");
        }
        return null;
    }
}
