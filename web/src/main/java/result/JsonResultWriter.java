package result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonResultWriter implements ResultWriter {

    private final JsonResult RESULT;
    private final int RESPONSE_CODE;

    public JsonResultWriter(JsonResult result, int responseCode) {
        this.RESULT = result;
        this.RESPONSE_CODE = responseCode;
    }

    @Override
    public void write(HttpServletResponse response) {
        try {
            response.setStatus(RESPONSE_CODE);
            response.getWriter().write(RESULT.getResult());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
