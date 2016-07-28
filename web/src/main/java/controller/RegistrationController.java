package controller;

import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import handler.HandlerRegister;
import handler.HandlerRegisterImpl;
import handler.UrlMethodPair;
import result.JSONResultWriter;
import service.InvalidUserDataException;
import service.UserService;
import service.impl.UserServiceImpl;
import service.impl.dto.RegistrationDto;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class RegistrationController implements Controller{

    private static RegistrationController instance;

    static  {
        getInstance();
    }

    private final UserService userService = UserServiceImpl.getInstance();

    private RegistrationController() {
        HandlerRegister handlerRegister = HandlerRegisterImpl.getInstance();

        UrlMethodPair postUrlMethodPair = new UrlMethodPair("/register", "POST");

        handlerRegister.register(postUrlMethodPair, ((request, response) -> {

            JSONResultWriter result = new JSONResultWriter();

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
                return result;
            }
            result.put("isRegistered", "true");
            result.put("message", "User has been successfully registered.");
            return result;
        }));

        UrlMethodPair getUrlMethodPair = new UrlMethodPair("/register", "GET");
        handlerRegister.register(getUrlMethodPair, ((request, response) -> {
            try {
                request.getRequestDispatcher("/").forward(request, response);
                return null;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }));
    }

    public static RegistrationController getInstance() {
        if (instance == null) {
            return instance = new RegistrationController();
        }
        return instance;
    }
}
