package servlet;

import controller.*;
import handler.*;
import result.ResultWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private final HandlerRegistry handlerRegistry;

    public DispatcherServlet() {
        super();
        ControllerInitializer controllerInitializer =
                new ControllerInitializer(
                        HandlerRegistryImpl.getInstance(),
                        Arrays.asList(
                            RegistrationController.getInstance(),
                            LoginController.getInstance(),
                            ChatController.getInstance()
                        )
                );
        handlerRegistry = controllerInitializer.getHandlerRegistry();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        handleRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handleRequest(req, resp);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        UrlMethodPair urlMethodPair = new UrlMethodPair(
                request.getServletPath(),
                HttpRequestMethod.forName(request.getMethod()));
        Optional<Handler> handler = handlerRegistry.getHandler(urlMethodPair);
        if (handler.isPresent()) {
            ResultWriter result = handler.get().processRequest(request, response);
            result.write(response);
        } else {
            try {
                response.getWriter().write("not found");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}