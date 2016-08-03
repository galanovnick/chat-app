package httpclient.testunit.impl;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static org.junit.Assert.fail;

public class Response {

    private final HttpResponse httpResponse;
    private final String content;
    private Map<String, String> jsonContent = null;

    public Response(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpResponse.getEntity().getContent()));
            StringBuilder resultContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                resultContent.append(line);
            }
            content = resultContent.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Response isJson() {
        if (!httpResponse.containsHeader("Content-Type") ||
            !httpResponse.getFirstHeader("Content-Type")
                    .getValue()
                    .contains("application/json")) {
            fail("Response is not JSON.");
        }
        return this;
    }

    public Response isHtml() {
        if (!httpResponse.containsHeader("Content-Type") ||
                !httpResponse.getFirstHeader("Content-Type")
                        .getValue()
                        .contains("text/html")) {
            fail("Response is not html.");
        }
        return this;
    }

    public Response isStatusCodeEquals(int statusCode) {
        if (httpResponse.getStatusLine().getStatusCode() != statusCode) {
            fail(String.format(
                    "Expected status code: %d, but found: %d",
                    statusCode,
                    httpResponse.getStatusLine().getStatusCode()));
        }
        return this;
    }

    public Response hasProperty(String propertyName, String propertyValue) {
        isJson();

        if (jsonContent == null) {
            jsonContent = new Gson().fromJson(content, Map.class);
        }
        if (!jsonContent.containsKey(propertyName)) {
            fail(String.format("Property \"%s\" not found", propertyName));
        }
        if (!jsonContent.get(propertyName).equals(propertyValue)) {
           fail(String.format(
                    "Expected property \"%s\" = \"%s\", but found \"%s\"",
                    propertyName, propertyValue, jsonContent.get(propertyName)));
        }
        return this;
    }
    public Response hasProperty(String propertyName) {
        isJson();

        if (jsonContent == null) {
            jsonContent = new Gson().fromJson(content, Map.class);
        }
        if (!jsonContent.containsKey(propertyName)) {
            fail(String.format("Property \"%s\" not found", propertyName));
        }
        return this;
    }

    public String getProperty(String propertyName) {
        isJson();
        hasProperty(propertyName);
        return jsonContent.get(propertyName);
    }
}
