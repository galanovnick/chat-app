package controller;

import handler.HandlerRegister;
import handler.UrlMethodPair;
import result.ResultWriter;

import java.io.IOException;

import static controller.HttpResponseMethod.GET;

abstract class AbstractChatApplicationController implements Controller {

    void handleGet(String url, HandlerRegister handlerRegister) {
        UrlMethodPair createChat = new UrlMethodPair(url, GET);
        handlerRegister.register(createChat, ((request, response) -> {
            try {
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
                resp.setStatus(403);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        };
    }
}
