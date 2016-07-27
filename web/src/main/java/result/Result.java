package result;

import javax.servlet.http.HttpServletResponse;

public interface Result {

    void write(HttpServletResponse response);
}
