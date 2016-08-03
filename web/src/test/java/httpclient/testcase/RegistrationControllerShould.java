package httpclient.testcase;

import httpclient.testunit.HttpClientTestUnit;
import httpclient.testunit.impl.HttpClientTestUnitImpl;
import httpclient.testunit.impl.Request;
import httpclient.testunit.impl.Response;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.apache.http.HttpStatus.SC_OK;

public class RegistrationControllerShould {

    private final String baseUrl = "http://localhost:8080/api/register";
    private final HttpClient client = HttpClientBuilder.create().build();
    private final HttpClientTestUnit testUnit = new HttpClientTestUnitImpl();

    @Test
    public void handleSuccessfullyUserRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", UUID.randomUUID().toString()));
        params.add(new BasicNameValuePair("password", "12345"));
        params.add(new BasicNameValuePair("passwordConfirm", "12345"));

        Response response = testUnit.sendPost(new Request(params, baseUrl, client));
        response
            .isStatusCodeEquals(SC_OK)
            .hasProperty("message", "User has been successfully registered.");

    }

    @Test
    public void handleUserWithDifferentPasswordsRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", UUID.randomUUID().toString()));
        params.add(new BasicNameValuePair("password", "54321"));
        params.add(new BasicNameValuePair("passwordConfirm", "12345"));

        Response response = testUnit.sendPost(new Request(params, baseUrl, client));
        response
            .isStatusCodeEquals(555)
            .hasProperty("message", "Passwords do not match.");
    }

    @Test
    public void handleUserWithEmptyFieldsRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", ""));
        params.add(new BasicNameValuePair("password", ""));
        params.add(new BasicNameValuePair("passwordConfirm", ""));

        Response response = testUnit.sendPost(new Request(params, baseUrl, client));
        response
            .isStatusCodeEquals(555)
            .hasProperty("message", "Fields cannot be empty.");
    }

    @Test
    public void handleDuplicatedUserRegistration() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", "Vasya"));
        params.add(new BasicNameValuePair("password", "123"));
        params.add(new BasicNameValuePair("passwordConfirm", "123"));

        testUnit.sendPost(new Request(params, baseUrl, client));

        Response response = testUnit.sendPost(new Request(params, baseUrl, client));
        response
            .isStatusCodeEquals(555)
            .hasProperty("message", "User with such name already exists.");
    }
}
