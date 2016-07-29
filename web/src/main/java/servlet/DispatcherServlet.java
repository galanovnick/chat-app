package servlet;

import controller.ChatController;
import controller.LoginController;
import controller.RegistrationController;
import controller.UserMenuController;
import handler.Handler;
import handler.HandlerRegister;
import handler.HandlerRegisterImpl;
import handler.UrlMethodPair;
import result.ResultWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    //TODO: response code 555 - business logic problem | not 200! | mb 500?
    private final HandlerRegister handlerRegister =
            HandlerRegisterImpl.getInstance();

    public DispatcherServlet() {
        handlerRegister.registerController(LoginController.class);
        handlerRegister.registerController(RegistrationController.class);
        handlerRegister.registerController(UserMenuController.class);
        handlerRegister.registerController(ChatController.class);
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

        UrlMethodPair urlMethodPair = new UrlMethodPair(request.getServletPath(), request.getMethod());
        Optional<Handler> handler = handlerRegister.getHandler(urlMethodPair);
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