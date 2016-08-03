package controller;

import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import handler.HandlerRegistry;
import handler.InMemoryHandlerRegistry;
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

    private LoginController(HandlerRegistry handlerRegistry) {
        registerLoginGet(handlerRegistry);
        registerLoginPost(handlerRegistry);

        registerUsernamePost(handlerRegistry);
        registerUsernameGet(handlerRegistry);
    }

    private void registerLoginPost(HandlerRegistry handlerRegistry) {
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

    private void registerLoginGet(HandlerRegistry handlerRegistry) {
        handleGet("/api/login", handlerRegistry);
    }

    private void registerUsernameGet(HandlerRegistry handlerRegistry) {
        handleGet("/api/username", handlerRegistry);
    }

    private void registerUsernamePost(HandlerRegistry handlerRegistry) {
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

    public static void instantiate(HandlerRegistry handlerRegistry) {
        if (instance == null) {
            instance = new LoginController(handlerRegistry);
        }
    }
}
