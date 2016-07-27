package servlet;

import controller.RegistrationController;
import handler.Handler;
import handler.HandlerRegistration;
import handler.HandlerRegistrationImpl;
import result.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {


    private final HandlerRegistration handlerRegistration =
            HandlerRegistrationImpl.getInstance();

    private final RegistrationController registrationController
            = RegistrationController.getInstance();

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

        Optional<Handler> handler = handlerRegistration.getHandler(request.getServletPath());
        if (handler.isPresent()) {
            Result result = handler.get().processRequest(request, response);
            result.write(response);
        } else {
            try {
                response.getWriter().write("not found");
                response.setStatus(404);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}