package controller;

import entity.AuthenticationToken;
import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import handler.HandlerRegistrationImpl;
import result.JSONResult;
import service.AuthenticationException;
import service.UserService;
import service.impl.UserServiceImpl;

import java.io.IOException;

public class LoginController {
    private static LoginController instance;

    private final UserService userService = UserServiceImpl.getInstance();

    private LoginController() {
        HandlerRegistrationImpl.getInstance().register("/login", ((request, response) -> {
            if (!request.getMethod().equalsIgnoreCase("post")) {
                try {
                    request.getRequestDispatcher("/").forward(request, response);
                    return null;
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }

            JSONResult result = new JSONResult();
            AuthenticationToken token;

            try {
                token = userService.login(new UserName(request.getParameter("username")),
                        new UserPassword(request.getParameter("password")));
            } catch (AuthenticationException e) {
                result.put("isAuthenticated", "false");
                result.put("message", e.getMessage());
                return result;
            }
            result.put("isAuthenticated", "true");
            result.put("token", token.getToken());
            return result;
        }));
    }

    public static LoginController getInstance() {
        if (instance == null) {
            return instance = new LoginController();
        }
        return instance;
    }
}
