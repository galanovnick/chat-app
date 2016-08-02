package result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonResultWriter implements ResultWriter {

    private final JsonResult result;
    private final int responseCode;

    public JsonResultWriter(JsonResult result, int responseCode) {
        this.result = result;
        this.responseCode = responseCode;
    }

    @Override
    public void write(HttpServletResponse response) {
        try {
            response.setStatus(responseCode);
            response.setContentType("application/json");
            response.getWriter().write(result.getResult());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
