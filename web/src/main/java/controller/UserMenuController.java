package controller;

import entity.AuthenticationToken;
import handler.HandlerRegister;
import handler.HandlerRegisterImpl;
import handler.UrlMethodPair;
import result.JSONResultWriter;
import service.UserService;
import service.impl.UserServiceImpl;
import service.impl.dto.AuthenticationTokenDto;

import java.io.IOException;
import java.util.Optional;

import static controller.HttpResponseMethod.GET;
import static controller.HttpResponseMethod.POST;

public class UserMenuController implements Controller {

    private static UserMenuController instance;

    static {
        getInstance();
    }

    private final UserService userService = UserServiceImpl.getInstance();

    private UserMenuController(){
        final HandlerRegister handlerRegister = HandlerRegisterImpl.getInstance();

        UrlMethodPair postUsernameRequest
                = new UrlMethodPair("/menu/username", POST);
        handlerRegister.register(postUsernameRequest, ((request, response) -> {
            Optional<AuthenticationToken> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token")
            ));

            if (token.isPresent()) {
                JSONResultWriter resultWriter = new JSONResultWriter();
                resultWriter.put("username",
                        userService.getUser(token.get().getUserId())
                                .getUsername());
                return resultWriter;
            } else {
                try {
                    request.getRequestDispatcher("/").forward(request, response);
                    return null;
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }));

        UrlMethodPair getUsernameRequest = new UrlMethodPair("/menu/username", GET);
        handlerRegister.register(getUsernameRequest, ((request, response) -> {
            try {
                request.getRequestDispatcher("/").forward(request, response);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            return null;
        }));
    }

    public static UserMenuController getInstance() {
        if (instance == null) {
            return instance = new UserMenuController();
        }
        return instance;
    }
}
