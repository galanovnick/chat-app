package controller;

import entity.tiny.UserName;
import entity.tiny.UserPassword;
import handler.HandlerRegistration;
import handler.HandlerRegistrationImpl;
import result.JSONResult;
import service.InvalidUserDataException;
import service.UserService;
import service.impl.UserServiceImpl;
import service.impl.dto.RegistrationDto;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class RegistrationController {

    private static RegistrationController instance;

    private final UserService userService = UserServiceImpl.getInstance();

    private RegistrationController() {
        HandlerRegistration handlerRegistration = HandlerRegistrationImpl.getInstance();
        handlerRegistration.register("/register", ((request, response) -> {

            if (!request.getMethod().equalsIgnoreCase("post")) {
                try {
                    request.getRequestDispatcher("/").forward(request, response);
                    return null;
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String passwordConfirm = request.getParameter("passwordConfirm");

            checkNotNull(username, "Username cannot be null");
            checkNotNull(password, "Password cannot be null");
            checkNotNull(passwordConfirm, "Password confirm cannot be null");

            JSONResult result = new JSONResult();

            RegistrationDto regDto = new RegistrationDto(
                    new UserName(username),
                    new UserPassword(password),
                    new UserPassword(passwordConfirm)
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
    }

    public static RegistrationController getInstance() {
        if (instance == null) {
            return instance = new RegistrationController();
        }
        return instance;
    }
}
