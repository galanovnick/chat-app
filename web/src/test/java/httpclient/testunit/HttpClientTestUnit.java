package httpclient.testunit;

import httpclient.testunit.impl.Request;
import httpclient.testunit.impl.Response;

public interface HttpClientTestUnit {

    Response sendPost(Request request);
    Response sendGet(Request request);
}
