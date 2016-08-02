package httpclient.testcase;

import httpclient.testunit.HttpClientTestUnit;
import httpclient.testunit.impl.HttpClientTestUnitImpl;
import httpclient.testunit.impl.Request;
import httpclient.testunit.impl.Response;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.util.UUID;

import static controller.HttpRequestMethod.GET;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DefaultControllerShould {

    private final String baseUrl = "http://localhost:8080/";
    private final HttpClient client = HttpClientBuilder.create().build();
    private final HttpClientTestUnit testUnit = new HttpClientTestUnitImpl();

    @Test
    public void handleGetOnMainPage() {
        Response response = testUnit.sendGet(new Request(baseUrl, client));
        response
            .isHtml()
            .isStatusCodeEquals(SC_OK);
    }

    @Test
    public void handleGetOnInvalidUrl() {
        Response response = testUnit.sendGet(new Request(
                baseUrl+ UUID.randomUUID().toString(), client));

        response.isStatusCodeEquals(SC_NOT_FOUND);
    }
}
