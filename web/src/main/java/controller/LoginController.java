package controller;

import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import handler.HandlerRegister;
import handler.HandlerRegisterImpl;
import handler.UrlMethodPair;
import result.JsonResult;
import result.JsonResultWriter;
import service.AuthenticationException;
import service.UserService;
import service.impl.UserServiceImpl;
import service.impl.dto.AuthenticationTokenDto;

import java.io.IOException;

import static controller.HttpResponseMethod.GET;
import static controller.HttpResponseMethod.POST;

public class LoginController implements Controller{
    private static LoginController instance;

    static {
        getInstance();
    }

    private final UserService userService = UserServiceImpl.getInstance();

    private final HandlerRegister handlerRegister = HandlerRegisterImpl.getInstance();

    private LoginController() {
        registerLoginGet();
        registerLoginPost();
    }

    private void registerLoginPost() {
        UrlMethodPair postUrlMethodPair = new UrlMethodPair("/login", POST);
        handlerRegister.register(postUrlMethodPair, ((request, response) -> {
            JsonResult result = new JsonResult();
            AuthenticationTokenDto token;

            try {
                token = userService.login(new UserName(request.getParameter("username")),
                        new UserPassword(request.getParameter("password")));
            } catch (AuthenticationException e) {
                result.put("isAuthenticated", "false");
                result.put("message", e.getMessage());
                return new JsonResultWriter(result, 555);
            }
            result.put("isAuthenticated", "true");
            result.put("token", token.getToken());

            return new JsonResultWriter(result, 200);
        }));
    }

    private void registerLoginGet() {
        UrlMethodPair getUrlMethodPair = new UrlMethodPair("/login", GET);
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
