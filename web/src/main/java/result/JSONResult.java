package result;

import com.sun.javafx.binding.StringFormatter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JSONResult implements Result {

    private final Map<String, String> content;

    public JSONResult() {
        this.content = new HashMap<>();
    }
    public JSONResult(Map<String, String> content) {
        this.content = content;
    }

    public void put(String key, String value) {
        content.put(key, value);
    }

    @Override
    public void write(HttpServletResponse response) {
        PrintWriter responseWriter;
        try {
            responseWriter = response.getWriter();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        final StringBuilder resultJSON = new StringBuilder("{");
        content.forEach((key, value) ->
                resultJSON.append(String.format("\"%s\": \"%s\",", key, value)));
        resultJSON.deleteCharAt(resultJSON.length() - 1);
        resultJSON.append("}");
        responseWriter.write(resultJSON.toString());
    }
}
