package httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static httpclient.HttpClientTestUtils.getResponseContent;
import static httpclient.HttpClientTestUtils.sendGet;
import static httpclient.HttpClientTestUtils.sendPost;
import static org.junit.Assert.assertEquals;

public class RegistrationControllerShould {

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

        assertEquals("Failed on root get request.", 404, response.getStatusLine().getStatusCode());
    }

    @Test
    public void handleSuccessfullyUserRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", UUID.randomUUID().toString()));
        params.add(new BasicNameValuePair("password", "12345"));
        params.add(new BasicNameValuePair("passwordConfirm", "12345"));

        HttpResponse response = sendPost(baseUrl + "register", params, client);

        String expectedContent = "{\"isRegistered\": \"true\"," +
                "\"message\": \"User has been successfully registered.\"}";

        String actualContent = getResponseContent(response, client);

        assertEquals("Failed on user registration post request.",
                expectedContent, actualContent);
    }

    @Test
    public void handleUserWithDifferentPasswordsRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", UUID.randomUUID().toString()));
        params.add(new BasicNameValuePair("password", "54321"));
        params.add(new BasicNameValuePair("passwordConfirm", "12345"));

        HttpResponse response = sendPost(baseUrl + "register", params, client);

        String expectedContent = "{\"isRegistered\": \"false\"," +
                "\"message\": \"Passwords do not match.\"}";

        String actualContent = getResponseContent(response, client);

        assertEquals("Failed on user registration post request.",
                expectedContent, actualContent);
    }

    @Test
    public void handleUserWithEmptyFieldsRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", ""));
        params.add(new BasicNameValuePair("password", ""));
        params.add(new BasicNameValuePair("passwordConfirm", ""));

        HttpResponse response = sendPost(baseUrl + "register", params, client);

        String expectedContent = "{\"isRegistered\": \"false\"," +
                "\"message\": \"Fields cannot be empty.\"}";

        String actualContent = getResponseContent(response, client);

        assertEquals("Failed on user registration post request.",
                expectedContent, actualContent);
    }

    @Test
    public void handleDuplicatedUserRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", "Vasya"));
        params.add(new BasicNameValuePair("password", "123"));
        params.add(new BasicNameValuePair("passwordConfirm", "123"));

        sendPost(baseUrl + "register", params, client);
        HttpResponse response = sendPost(baseUrl + "register", params, client);

        String expectedContent = "{\"isRegistered\": \"false\"," +
                "\"message\": \"User with such name already exists.\"}";

        String actualContent = getResponseContent(response, client);

        assertEquals("Failed on user registration post request.",
                expectedContent, actualContent);
    }
}
