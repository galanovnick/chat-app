package controller;

import handler.HandlerRegistry;
import handler.UrlMethodPair;
import result.ResultWriter;

import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

import static controller.HttpRequestMethod.GET;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED;

abstract class AbstractChatApplicationController implements Controller {

    void handleGet(String url, HandlerRegistry handlerRegistry) {
        UrlMethodPair createChat = new UrlMethodPair(url, GET);
        handlerRegistry.register(createChat, ((request, response) -> {
            try {
                System.out.println("forward to \"/\"");
                request.getRequestDispatcher("/").forward(request, response);
                return null;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }));
    }

    ResultWriter authenticationRequiredErrorWriter() {
        return resp -> {
            try {
                resp.getWriter().write("Authentication required.");
                resp.setStatus(SC_FORBIDDEN);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        };
    }
}
