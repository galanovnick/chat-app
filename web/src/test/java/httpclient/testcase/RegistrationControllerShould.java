package httpclient.testcase;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static httpclient.testcase.HttpClientTestUtils.getResponseContent;
import static httpclient.testcase.HttpClientTestUtils.sendPost;
import static org.junit.Assert.assertEquals;

public class RegistrationControllerShould {

    private final String baseUrl = "http://localhost:8080/register";
    private final HttpClient client = HttpClientBuilder.create().build();

    @Test
    public void handleSuccessfullyUserRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", UUID.randomUUID().toString()));
        params.add(new BasicNameValuePair("password", "12345"));
        params.add(new BasicNameValuePair("passwordConfirm", "12345"));

        HttpResponse response = sendPost(baseUrl, params, client);

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

        HttpResponse response = sendPost(baseUrl, params, client);

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

        HttpResponse response = sendPost(baseUrl, params, client);

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

        sendPost(baseUrl, params, client);
        HttpResponse response = sendPost(baseUrl, params, client);

        String expectedContent = "{\"isRegistered\": \"false\"," +
                "\"message\": \"User with such name already exists.\"}";

        String actualContent = getResponseContent(response);

        assertEquals("Failed on user registration post request.",
                expectedContent, actualContent);
    }
}
