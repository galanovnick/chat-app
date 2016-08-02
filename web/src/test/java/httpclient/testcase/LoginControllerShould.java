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

public class LoginControllerShould {

    private final String baseUrl = "http://localhost:8080/api/login";
    private final HttpClient client = HttpClientBuilder.create().build();

    private final HttpClientTestUnit testUnit = new HttpClientTestUnitImpl();

    @Test
    public void handleUnregisteredUserAuthentication() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", UUID.randomUUID().toString()));
        params.add(new BasicNameValuePair("password", "123"));

        Response response = testUnit.sendPost(new Request(params, baseUrl, client));
        response
            .isStatusCodeEquals(555)
            .isJson()
                .hasProperty("message", "Invalid username or password.");
    }

    @Test
    public void handleUserWithEmptyFieldsAuthentication() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", ""));
        params.add(new BasicNameValuePair("password", ""));

        Response response = testUnit.sendPost(new Request(params, baseUrl, client));
        response
            .isStatusCodeEquals(555)
            .isJson()
                .hasProperty("message", "Fields cannot be empty.");
    }

    @Test
    public void handleUserAuthentication() {
        List<NameValuePair> params = new ArrayList<>();
        String username = UUID.randomUUID().toString();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", "123"));
        params.add(new BasicNameValuePair("passwordConfirm", "123"));

        Response regUserResponse = testUnit.sendPost(new Request(params,
                "http://localhost:8080/api/register", client));
        regUserResponse
            .isStatusCodeEquals(SC_OK)
            .isJson()
                .hasProperty("message", "User has been successfully registered.");


        params.clear();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", "123"));

        Response loginResponse = testUnit.sendPost(new Request(params, baseUrl, client));
        loginResponse
            .isStatusCodeEquals(SC_OK)
            .isJson();
    }
}
