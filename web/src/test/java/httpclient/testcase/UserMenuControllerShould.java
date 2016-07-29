package httpclient.testcase;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static httpclient.testcase.HttpClientTestUtils.getResponseContent;
import static httpclient.testcase.HttpClientTestUtils.sendPost;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserMenuControllerShould {

    private final String baseUrl = "http://localhost:8080/";
    private final HttpClient client = HttpClientBuilder.create().build();

    private final String username = UUID.randomUUID().toString();
    private String token;

    @Before
    public void before() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", "12345"));
        params.add(new BasicNameValuePair("passwordConfirm", "12345"));

        HttpResponse registrationResponse = sendPost(baseUrl + "/register", params, client);
        if (registrationResponse == null) {
            fail("Failed due null response.");
        }
        String expectedContent = "{\"isRegistered\": \"true\"," +
                "\"message\": \"User has been successfully registered.\"}";

        String actualContent = getResponseContent(registrationResponse);

        assertEquals("Failed on user registration post request.",
                expectedContent, actualContent);

        params.clear();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", "12345"));

        HttpResponse authenticationResponse = sendPost(baseUrl + "/login", params, client);
        if (authenticationResponse == null) {
            fail("Failed due null response.");
        }
        assertEquals("Failed on user authentication.", 200,
                authenticationResponse.getStatusLine().getStatusCode());

        String responseContent = getResponseContent(authenticationResponse);

        Pattern pattern = Pattern.compile("\"token\": \"(.+)\"");
        Matcher matcher = pattern.matcher(responseContent);
        System.out.println(responseContent);
        if (matcher.find()) {
            token = matcher.group(1);
            System.out.println(token);
        } else {
            fail("Failed due invalid authentication response.");
        }
    }

    @Test
    public void provideAuthenticatedUserNameInJson() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));

        HttpResponse response = sendPost(baseUrl + "/menu/username", params, client);

        if (response == null) {
            fail("Failed due null response.");
        }

        assertEquals("Failed due incorrect response code.", 200,
                response.getStatusLine().getStatusCode());

        String expected = String.format("{\"username\": \"%s\"}", username);

        String actual = getResponseContent(response);

        assertEquals("Failed due incorrect response content.", expected, actual);
    }

    @Test
    public void provideAvailableChatListInJson() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));

        HttpResponse response = sendPost(baseUrl + "/menu/chats", params, client);

        if (response == null) {
            fail("Failed due null response.");
        }

        assertEquals("Failed due incorrect response code.", 200,
                response.getStatusLine().getStatusCode());

        String expected = "{\"chats\": []}";

        String actual = getResponseContent(response);

        assertEquals("Failed due incorrect response content.", expected, actual);
    }
}
