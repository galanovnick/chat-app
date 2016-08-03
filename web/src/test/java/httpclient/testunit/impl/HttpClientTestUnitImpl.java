package httpclient.testunit.impl;

import httpclient.testunit.HttpClientTestUnit;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;

import static org.apache.http.HttpHeaders.USER_AGENT;
import static org.junit.Assert.fail;

public class HttpClientTestUnitImpl implements HttpClientTestUnit {

    @Override
    public Response sendPost(Request request) {
        HttpPost post = new HttpPost(request.getUrl());
        post.setHeader("User-Agent", USER_AGENT);

        try {
            post.setEntity(new UrlEncodedFormEntity(request.getParams()));
            return new Response(request.getClient().execute(post));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception.");
        }
        return null;
    }

    @Override
    public Response sendGet(Request request) {
        HttpGet get = new HttpGet(request.getUrl());
        try {
            return new Response(request.getClient().execute(get));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception.");
        }
        return null;
    }

    @Override
    public Response sendDelete(Request request) {
        HttpDelete delete = new HttpDelete(request.getUrl());
        try {
            return new Response(request.getClient().execute(delete));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception.");
        }
        return null;
    }
}
