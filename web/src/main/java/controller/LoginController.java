package controller;

import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import handler.HandlerRegister;
import handler.HandlerRegisterImpl;
import handler.UrlMethodPair;
import result.JSONResultWriter;
import service.AuthenticationException;
import service.UserService;
import service.impl.UserServiceImpl;
import service.impl.dto.AuthenticatedUserDto;

import java.io.IOException;

public class LoginController implements Controller{
    private static LoginController instance;

    static {
        getInstance();
    }

    private final UserService userService = UserServiceImpl.getInstance();

    private LoginController() {
        UrlMethodPair postUrlMethodPair = new UrlMethodPair("/login", "POST");

        HandlerRegister handlerRegister = HandlerRegisterImpl.getInstance();

        handlerRegister.register(postUrlMethodPair, ((request, response) -> {
            JSONResultWriter result = new JSONResultWriter();
            AuthenticatedUserDto user;

            try {
                user = userService.login(new UserName(request.getParameter("username")),
                        new UserPassword(request.getParameter("password")));
            } catch (AuthenticationException e) {
                result.put("isAuthenticated", "false");
                result.put("message", e.getMessage());
                return result;
            }
            result.put("isAuthenticated", "true");
            result.put("token", user.getToken());
            return result;
        }));

        UrlMethodPair getUrlMethodPair = new UrlMethodPair("/login", "GET");
        handlerRegister.register(getUrlMethodPair, ((request, response) -> {
            try {
                request.getRequestDispatcher("/").forward(request, response);
                return null;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }));
    }

    public static LoginController getInstance() {
        if (instance == null) {
            return instance = new LoginController();
        }
        return instance;
    }
}
