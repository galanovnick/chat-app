package controller;

import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import handler.HandlerRegistry;
import handler.UrlMethodPair;
import result.JsonResult;
import result.JsonResultWriter;
import service.InvalidUserDataException;
import service.UserService;
import service.impl.UserServiceImpl;
import service.impl.dto.RegistrationDto;

import static controller.ControllerConstants.*;
import static controller.HttpRequestMethod.*;

public class RegistrationController
        extends AbstractChatApplicationController
        implements Controller{

    private static RegistrationController instance;

    private final UserService userService = UserServiceImpl.getInstance();

    private RegistrationController(HandlerRegistry handlerRegistry) {
        registerRegistrationGet(handlerRegistry);
        registerRegistrationPost(handlerRegistry);
    }

    private void registerRegistrationPost(HandlerRegistry handlerRegistry) {
        UrlMethodPair postUrlMethodPair = new UrlMethodPair("/api/register", POST);

        handlerRegistry.register(postUrlMethodPair, ((request, response) -> {

            JsonResult result = new JsonResult();

            RegistrationDto regDto = new RegistrationDto(
                    new UserName(request.getParameter(USERNAME_PARAMETER)),
                    new UserPassword(request.getParameter(PASSWORD_PARAMETER)),
                    new UserPassword(request.getParameter(PASSWORD_CONFIRM_PARAMETER))
            );

            try {
                userService.registerUser(regDto);
            } catch (InvalidUserDataException e) {
                result.put(MESSAGE_PARAMETER, e.getMessage());
                return new JsonResultWriter(result, 555);
            }
            result.put(MESSAGE_PARAMETER, "User has been successfully registered.");
            return new JsonResultWriter(result, 200);
        }));
    }
    private void registerRegistrationGet(HandlerRegistry handlerRegistry) {
        handleGet("/api/register", handlerRegistry);
    }
    public static void instantiate(HandlerRegistry handlerRegistry) {
        if (instance == null) {
            instance = new RegistrationController(handlerRegistry);
        }
    }
}
