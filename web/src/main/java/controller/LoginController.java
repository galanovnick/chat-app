package controller;

import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import handler.HandlerRegistry;
import handler.HandlerRegistryImpl;
import handler.UrlMethodPair;
import result.JsonResult;
import result.JsonResultWriter;
import service.AuthenticationException;
import service.UserService;
import service.impl.UserServiceImpl;
import service.impl.dto.AuthenticationTokenDto;

import java.util.Optional;

import static controller.ControllerConstants.*;
import static controller.HttpRequestMethod.POST;

public class LoginController
        extends AbstractChatApplicationController
        implements Controller{

    private static LoginController instance;

    private final UserService userService = UserServiceImpl.getInstance();

    private final HandlerRegistry handlerRegistry = HandlerRegistryImpl.getInstance();

    private LoginController() {
        registerLoginGet();
        registerLoginPost();

        registerUsernamePost();
        registerUsernameGet();
    }

    private void registerLoginPost() {
        UrlMethodPair postUrlMethodPair = new UrlMethodPair("/api/login", POST);
        handlerRegistry.register(postUrlMethodPair, ((request, response) -> {
            JsonResult result = new JsonResult();
            AuthenticationTokenDto token;

            try {
                token = userService.login(new UserName(request.getParameter(USERNAME_PARAMETER)),
                        new UserPassword(request.getParameter(PASSWORD_PARAMETER)));
            } catch (AuthenticationException e) {
                result.put(MESSAGE_PARAMETER, e.getMessage());
                return new JsonResultWriter(result, 555);
            }
            result.put(TOKEN_PARAMETER, token.getToken());
            return new JsonResultWriter(result, 200);
        }));
    }

    private void registerLoginGet() {
        handleGet("/api/login", handlerRegistry);
    }

    private void registerUsernameGet() {
        handleGet("/api/username", handlerRegistry);
    }

    private void registerUsernamePost() {
        UrlMethodPair postUsernameRequest
                = new UrlMethodPair("/api/username", POST);
        handlerRegistry.register(postUsernameRequest, ((request, response) -> {
            Optional<AuthenticationTokenDto> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter(TOKEN_PARAMETER))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();
                result.put(USERNAME_PARAMETER,
                        userService.getUser(token.get().getUserId())
                                .getUsername());
                return new JsonResultWriter(result, 200);
            } else {
                return authenticationRequiredErrorWriter();
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
