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

import static httpclient.HttpClientTestUtils.getResponseContent;
import static httpclient.HttpClientTestUtils.sendPost;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LoginControllerShould {

    private final String baseUrl = "http://localhost:8080/login";
    private HttpClient client = HttpClientBuilder.create().build();

    @Test
    public void handleUnregisteredUserAuthentication() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", UUID.randomUUID().toString()));
        params.add(new BasicNameValuePair("password", "123"));

        HttpResponse response = sendPost(baseUrl, params, client);

        if (response == null) {
            fail("Failed due null response.");
        }

        String expected = "{\"isAuthenticated\": \"false\"," +
                "\"message\": \"Invalid username or password.\"}";
        String actual = getResponseContent(response);
        assertEquals("Failed unregistered user authentication.", 555,
                response.getStatusLine().getStatusCode());
        assertEquals("Failed unregistered user authentication.", expected, actual);
    }

    @Test
    public void handleUserWithEmptyFieldsAuthentication() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", ""));
        params.add(new BasicNameValuePair("password", ""));

        HttpResponse response = sendPost(baseUrl, params, client);

        if (response == null) {
            fail("Failed due null response.");
        }

        String expected = "{\"isAuthenticated\": \"false\"," +
                "\"message\": \"Fields cannot be empty.\"}";
        String actual = getResponseContent(response);

        assertEquals("Failed unregistered user authentication.", 555,
                response.getStatusLine().getStatusCode());
        assertEquals("Failed unregistered user authentication.", expected, actual);
    }

    @Test
    public void handleUserAuthentication() {
        List<NameValuePair> params = new ArrayList<>();
        String username = UUID.randomUUID().toString();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", "123"));
        params.add(new BasicNameValuePair("passwordConfirm", "123"));

        HttpResponse response = sendPost("http://localhost:8080/register", params, client);

        if (response == null) {
            fail("Failed due null response.");
        }

        String responseContent = getResponseContent(response);
        String successfulRegistrationResult = "{\"isRegistered\": \"true\"," +
                "\"message\": \"User has been successfully registered.\"}";
        if (!responseContent.equals(successfulRegistrationResult)) {
            System.out.println(responseContent);
            fail("Failed on user registration.");
        }

        params.clear();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", "123"));

        response = sendPost(baseUrl, params, client);

        if (response == null) {
            fail("Failed due null response.");
        }

        assertEquals("Failed on user authentication.", 200,
                response.getStatusLine().getStatusCode());
    }
}
