package controller;

import entity.AuthenticationToken;
import handler.HandlerRegister;
import handler.HandlerRegisterImpl;
import handler.UrlMethodPair;
import result.JsonResult;
import result.JsonResultWriter;
import service.ChatService;
import service.UserService;
import service.impl.ChatServiceImpl;
import service.impl.UserServiceImpl;
import service.impl.dto.AuthenticationTokenDto;
import service.impl.dto.ChatDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static controller.HttpResponseMethod.GET;
import static controller.HttpResponseMethod.POST;

public class UserMenuController implements Controller {

    private static UserMenuController instance;

    static {
        getInstance();
    }

    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    private final HandlerRegister handlerRegister = HandlerRegisterImpl.getInstance();

    private UserMenuController(){
        registerUsernamePost();
        registerUsernameGet();

        registerChatListPost();
        registerChatListGet();
    }

    private void registerChatListGet() {
        UrlMethodPair getChatListRequest = new UrlMethodPair("/menu/chats", GET);
        handlerRegister.register(getChatListRequest, ((request, response) -> {
            try {
                request.getRequestDispatcher("/").forward(request, response);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            return null;
        }));
    }

    private void registerChatListPost() {
        UrlMethodPair postChatListRequest
                = new UrlMethodPair("/menu/chats", POST);
        handlerRegister.register(postChatListRequest, ((request, response) -> {
            Optional<AuthenticationToken> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token"))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();

                Collection<ChatDto> allChats = chatService.getAllChats();
                List<String> chatsList = new ArrayList<>();
                allChats.forEach((chat) -> {
                    JsonResult chats = new JsonResult();
                    chats.put("chatId", chat.getChatId().value().toString());
                    chats.put("chatName", chat.getChatName());
                    chatsList.add(chats.getResult());
                });
                result.put("chats", chatsList.toArray(new String[chatsList.size()]));
                return new JsonResultWriter(result, 200);
            } else {
                try {
                    request.getRequestDispatcher("/").forward(request, response);
                    return null;
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }));
    }

    private void registerUsernameGet() {
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

    private void registerUsernamePost() {
        UrlMethodPair postUsernameRequest
                = new UrlMethodPair("/menu/username", POST);
        handlerRegister.register(postUsernameRequest, ((request, response) -> {
            Optional<AuthenticationToken> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token"))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();
                result.put("username",
                        userService.getUser(token.get().getUserId())
                                .getUsername());
                return new JsonResultWriter(result, 200);
            } else {
                try {
                    request.getRequestDispatcher("/").forward(request, response);
                    return null;
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }));
    }

    public static UserMenuController getInstance() {
        if (instance == null) {
            return instance = new UserMenuController();
        }
        return instance;
    }
}
