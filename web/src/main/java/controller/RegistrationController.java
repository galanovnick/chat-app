package controller;

import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import handler.HandlerRegister;
import handler.HandlerRegisterImpl;
import handler.UrlMethodPair;
import result.JsonResult;
import result.JsonResultWriter;
import service.InvalidUserDataException;
import service.UserService;
import service.impl.UserServiceImpl;
import service.impl.dto.RegistrationDto;

import static controller.HttpResponseMethod.*;

public class RegistrationController
        extends AbstractChatApplicationController
        implements Controller{

    private static RegistrationController instance;

    static {
        getInstance();
    }

    private final UserService userService = UserServiceImpl.getInstance();

    private final HandlerRegister handlerRegister = HandlerRegisterImpl.getInstance();
    private RegistrationController() {
        registerRegistrationGet();
        registerRegistrationPost();
    }

    private void registerRegistrationPost() {
        UrlMethodPair postUrlMethodPair = new UrlMethodPair("/register", POST);

        handlerRegister.register(postUrlMethodPair, ((request, response) -> {

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
        handleGet("/register", handlerRegister);
    }
    public static RegistrationController getInstance() {
        if (instance == null) {
            return instance = new RegistrationController();
        }
        return instance;
    }
}
