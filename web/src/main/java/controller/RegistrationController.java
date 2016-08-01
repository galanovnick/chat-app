package controller;

import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import handler.HandlerRegistry;
import handler.HandlerRegistryImpl;
import handler.UrlMethodPair;
import result.JsonResult;
import result.JsonResultWriter;
import service.InvalidUserDataException;
import service.UserService;
import service.impl.UserServiceImpl;
import service.impl.dto.RegistrationDto;

import static controller.HttpRequestMethod.*;

public class RegistrationController
        extends AbstractChatApplicationController
        implements Controller{

    private static RegistrationController instance;

    private final UserService userService = UserServiceImpl.getInstance();

    private final HandlerRegistry handlerRegistry = HandlerRegistryImpl.getInstance();

    private RegistrationController() {
        registerRegistrationGet();
        registerRegistrationPost();
    }

    private void registerRegistrationPost() {
        UrlMethodPair postUrlMethodPair = new UrlMethodPair("/register", POST);

        handlerRegistry.register(postUrlMethodPair, ((request, response) -> {

            JsonResult result = new JsonResult();

            RegistrationDto regDto = new RegistrationDto(
                    new UserName(request.getParameter("username")),
                    new UserPassword(request.getParameter("password")),
                    new UserPassword(request.getParameter("passwordConfirm"))
            );

            try {
                userService.registerUser(regDto);
            } catch (InvalidUserDataException e) {
                result.put("isRegistered", "false");
                result.put("message", e.getMessage());
                return new JsonResultWriter(result, 555);
            }
            result.put("isRegistered", "true");
            result.put("message", "User has been successfully registered.");
            return new JsonResultWriter(result, 200);
        }));
    }
    private void registerRegistrationGet() {
        handleGet("/register", handlerRegistry);
    }
    public static RegistrationController getInstance() {
        if (instance == null) {
            return instance = new RegistrationController();
        }
        return instance;
    }
}
