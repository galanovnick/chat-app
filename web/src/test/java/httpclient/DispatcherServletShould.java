package httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.apache.http.HttpHeaders.USER_AGENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DispatcherServletShould {

    private final String baseUrl = "http://localhost:8080/";
    private HttpClient client = HttpClientBuilder.create().build();

    @Test
    public void handleGetOnMainPage() {
        HttpResponse response = sendGet(baseUrl);

        assertEquals("Failed on root get request.", 200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void handleSuccessfullyUserRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", UUID.randomUUID().toString()));
        params.add(new BasicNameValuePair("password", "12345"));
        params.add(new BasicNameValuePair("passwordConfirm", "12345"));

        HttpResponse response = sendPost(baseUrl + "register", params);

        String expectedContent = "{\"isRegistered\": \"true\"," +
                "\"message\": \"User has been successfully registered.\"}";

        String actualContent = getResponseContent(response);

        assertEquals("Failed on user registration post request.",
                expectedContent, actualContent);
    }

    @Test
    public void handleUserWithDifferentPasswordsRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", UUID.randomUUID().toString()));
        params.add(new BasicNameValuePair("password", "54321"));
        params.add(new BasicNameValuePair("passwordConfirm", "12345"));

        HttpResponse response = sendPost(baseUrl + "register", params);

        String expectedContent = "{\"isRegistered\": \"false\"," +
                "\"message\": \"Passwords do not match.\"}";

        String actualContent = getResponseContent(response);

        assertEquals("Failed on user registration post request.",
                expectedContent, actualContent);
    }

    @Test
    public void handleUserWithEmptyFieldsRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", ""));
        params.add(new BasicNameValuePair("password", ""));
        params.add(new BasicNameValuePair("passwordConfirm", ""));

        HttpResponse response = sendPost(baseUrl + "register", params);

        String expectedContent = "{\"isRegistered\": \"false\"," +
                "\"message\": \"Fields cannot be empty.\"}";

        String actualContent = getResponseContent(response);

        assertEquals("Failed on user registration post request.",
                expectedContent, actualContent);
    }

    @Test
    public void handleDuplicatedUserRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", "Vasya"));
        params.add(new BasicNameValuePair("password", "123"));
        params.add(new BasicNameValuePair("passwordConfirm", "123"));

        sendPost(baseUrl + "register", params);
        HttpResponse response = sendPost(baseUrl + "register", params);

        String expectedContent = "{\"isRegistered\": \"false\"," +
                "\"message\": \"User with such name already exists.\"}";

        String actualContent = getResponseContent(response);

        assertEquals("Failed on user registration post request.",
                expectedContent, actualContent);
    }

    private String getResponseContent(HttpResponse response) {
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

    private HttpResponse sendGet(String url) {
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed on main page get request.");
        }
        return response;
    }

    private HttpResponse sendPost(String url, List<NameValuePair> params) {
        HttpPost request = new HttpPost(url);
        request.setHeader("User-Agent", USER_AGENT);

        try {
            request.setEntity(new UrlEncodedFormEntity(params));
            return client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed on user registration post request.");
        }
        return null;
    }
}
