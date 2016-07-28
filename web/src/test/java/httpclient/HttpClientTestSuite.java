package httpclient;

import httpclient.testcase.DefaultControllerShould;
import httpclient.testcase.LoginControllerShould;
import httpclient.testcase.RegistrationControllerShould;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    DefaultControllerShould.class,
    RegistrationControllerShould.class,
    LoginControllerShould.class
})
public class HttpClientTestSuite {
}
