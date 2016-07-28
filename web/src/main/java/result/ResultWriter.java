package result;

import javax.servlet.http.HttpServletResponse;

public interface ResultWriter {

    void write(HttpServletResponse response);
}
