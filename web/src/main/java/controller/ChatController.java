package controller;

import entity.AuthenticationToken;
import entity.Message;
import entity.tiny.chat.ChatId;
import entity.tiny.user.UserId;
import handler.HandlerRegister;
import handler.HandlerRegisterImpl;
import handler.UrlMethodPair;
import result.JsonResult;
import result.JsonResultWriter;
import service.ChatService;
import service.InvalidChatNameException;
import service.UserAlreadyInChatException;
import service.UserService;
import service.impl.ChatServiceImpl;
import service.impl.UserServiceImpl;
import service.impl.dto.AuthenticationTokenDto;
import service.impl.dto.MessageDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static controller.HttpResponseMethod.GET;
import static controller.HttpResponseMethod.POST;

public class ChatController implements Controller {
    private static ChatController instance;

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    private final HandlerRegister handlerRegister = HandlerRegisterImpl.getInstance();

    static {
        getInstance();
    }

    private ChatController() {
        registerCreateChatPost();
        registerCreateChatGet();

        registerJoinUserPost();
        registerJoinUserGet();

        registerLeaveUserPost();
        registerLeaveUserGet();

        registerAddMessagePost();
        registerAddMessageGet();

        registerMessagesPost();
        registerMessagesGet();
    }

    private void registerMessagesGet() {
        handleGet("/chat/messages");
    }

    private void registerMessagesPost() {
        UrlMethodPair createChatPair = new UrlMethodPair("/chat/messages", POST);
        handlerRegister.register(createChatPair, ((request, response) -> {
            Optional<AuthenticationToken> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token"))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();

                Collection<MessageDto> messages = chatService.getAllChatMessages(
                        new ChatId(Long.parseLong(request.getParameter("chatId"))));
                List<String> messagesList =
                        messages.stream()
                                .map(message ->
                                        String.format("%s: %s",
                                                message.getAuthorName(),
                                                message.getText()))
                                .collect(Collectors.toList());
                result.put("messages", messagesList.toArray(new String[messagesList.size()]));
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

    private void registerAddMessageGet() {
        handleGet("/chat/add-message");
    }

    private void registerAddMessagePost() {
        UrlMethodPair createChatPair = new UrlMethodPair("/chat/add-message", POST);
        handlerRegister.register(createChatPair, ((request, response) -> {
            Optional<AuthenticationToken> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token"))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();

                chatService.addMessage(
                        new ChatId(Long.parseLong(request.getParameter("chatId"))),
                        new Message(
                                userService.getUser(token.get().getUserId()).getUsername(),
                                request.getParameter("message"))
                );
                result.put("isMessageAdded", "true");
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

    private void registerLeaveUserGet() {
        handleGet("/chat/leave");
    }

    private void registerLeaveUserPost() {
        UrlMethodPair createChatPair = new UrlMethodPair("/chat/leave", POST);
        handlerRegister.register(createChatPair, ((request, response) -> {
            Optional<AuthenticationToken> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token"))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();

                chatService.leaveChat(
                        new ChatId(Long.parseLong(request.getParameter("chatId"))),
                        token.get().getUserId()
                );
                result.put("isLeft", "true");
                result.put("message", "User successfully left.");
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

    private void registerJoinUserGet() {
        handleGet("/chat/join");
    }

    private void registerJoinUserPost() {
        UrlMethodPair createChatPair = new UrlMethodPair("/chat/join", POST);
        handlerRegister.register(createChatPair, ((request, response) -> {
            Optional<AuthenticationToken> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token"))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();

                try {
                    chatService.joinChat(
                            new ChatId(Long.parseLong(request.getParameter("chatId"))),
                           token.get().getUserId()
                    );
                    result.put("isJoined", "true");
                    result.put("message", "User successfully joined.");
                    return new JsonResultWriter(result, 200);
                } catch (UserAlreadyInChatException e) {
                    result.put("isJoined", "false");
                    result.put("message", e.getMessage());
                    return new JsonResultWriter(result, 555);
                }
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

    private void registerCreateChatGet() {
        handleGet("/chat/new");
    }

    private void registerCreateChatPost() {
        UrlMethodPair createChatPair = new UrlMethodPair("/chat/new", POST);
        handlerRegister.register(createChatPair, ((request, response) -> {
            Optional<AuthenticationToken> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token"))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();
                try {
                    ChatId chatId = chatService.createChat(
                            request.getParameter("chatName"),
                            token.get().getUserId()
                    );
                    result.put("isCreated", "true");
                    result.put("chatId", chatId.value().toString());

                    return new JsonResultWriter(result, 200);
                } catch (InvalidChatNameException e) {
                    result.put("isCreated", "false");
                    result.put("message", e.getMessage());

                    return new JsonResultWriter(result, 555);
                }
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

    private void handleGet(String url) {
        UrlMethodPair createChat = new UrlMethodPair(url, GET);
        handlerRegister.register(createChat, ((request, response) -> {
            try {
                request.getRequestDispatcher("/").forward(request, response);
                return null;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }));
    }

    public static ChatController getInstance() {
        if (instance == null) {
            return instance = new ChatController();
        }
        return instance;
    }
}
