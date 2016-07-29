package httpclient;

import httpclient.testcase.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    DefaultControllerShould.class,
    RegistrationControllerShould.class,
    LoginControllerShould.class,
    UserMenuControllerShould.class,
    ChatControllerShould.class
})
public class HttpClientTestSuite {
}
