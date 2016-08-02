package controller;

import entity.Message;
import entity.tiny.chat.ChatId;
import handler.HandlerRegistry;
import handler.HandlerRegistryImpl;
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
import service.impl.dto.ChatDto;
import service.impl.dto.MessageDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static controller.HttpRequestMethod.POST;

public class ChatController
        extends AbstractChatApplicationController
        implements Controller {
    private static ChatController instance;

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    private final HandlerRegistry handlerRegistry = HandlerRegistryImpl.getInstance();

    private ChatController() {
        registerChatListPost();
        registerChatListGet();

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
        handleGet("/api/chat/messages", handlerRegistry);
    }

    private void registerMessagesPost() {
        UrlMethodPair createChatPair = new UrlMethodPair("/api/chat/messages", POST);
        handlerRegistry.register(createChatPair, ((request, response) -> {
            Optional<AuthenticationTokenDto> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token"))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();

                Collection<MessageDto> allChatMessages =
                        chatService.getAllChatMessages(
                                new ChatId(Long.parseLong(request.getParameter("chatId"))));
                List<String> messageList = new ArrayList<>();
                allChatMessages.forEach((message) -> {
                    JsonResult messageResult = new JsonResult();
                    messageResult.put("text", message.getText());
                    messageResult.put("username", message.getAuthorName());
                    messageList.add(messageResult.getResult());
                });
                result.put("messages", messageList.toArray(new String[messageList.size()]));
                return new JsonResultWriter(result, 200);
            } else {
                return authenticationRequiredErrorWriter();
            }
        }));
    }

    private void registerAddMessageGet() {
        handleGet("/api/chat/add-message", handlerRegistry);
    }

    private void registerAddMessagePost() {
        UrlMethodPair createChatPair = new UrlMethodPair("/api/chat/add-message", POST);
        handlerRegistry.register(createChatPair, ((request, response) -> {
            Optional<AuthenticationTokenDto> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token"))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();
                Long chatId = Long.parseLong(request.getParameter("chatId"));
                chatService.addMessage(
                        new ChatId(chatId),
                        new Message(
                                userService.getUser(token.get().getUserId()).getUsername(),
                                request.getParameter("message"))
                );
                result.put("roomId", chatId.toString());
                Collection<MessageDto> allChatMessages =
                        chatService.getAllChatMessages(new ChatId(chatId));
                List<String> messageList = new ArrayList<>();
                allChatMessages.forEach((message) -> {
                    JsonResult messageResult = new JsonResult();
                    messageResult.put("text", message.getText());
                    messageResult.put("username", message.getAuthorName());
                    messageList.add(messageResult.getResult());
                });
                result.put("messages", messageList.toArray(new String[messageList.size()]));
                return new JsonResultWriter(result, 200);
            } else {
                return authenticationRequiredErrorWriter();
            }
        }));
    }

    private void registerLeaveUserGet() {
        handleGet("/api/chat/leave", handlerRegistry);
    }

    private void registerLeaveUserPost() {
        UrlMethodPair createChatPair = new UrlMethodPair("/api/chat/leave", POST);
        handlerRegistry.register(createChatPair, ((request, response) -> {
            Optional<AuthenticationTokenDto> token = userService.checkAuthentication(
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
                return authenticationRequiredErrorWriter();
            }
        }));
    }

    private void registerJoinUserGet() {
        handleGet("/api/chat/join", handlerRegistry);
    }

    private void registerJoinUserPost() {
        UrlMethodPair createChatPair = new UrlMethodPair("/api/chat/join", POST);
        handlerRegistry.register(createChatPair, ((request, response) -> {
            Optional<AuthenticationTokenDto> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token"))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();

                Long chatId = Long.parseLong(request.getParameter("chatId"));
                try {
                    chatService.joinChat(
                            new ChatId(chatId),
                           token.get().getUserId()
                    );
                    result.put("isJoined", "true");
                    result.put("chatId", chatId.toString());
                    result.put("chatName",
                            chatService.getChat(new ChatId(chatId)).getChatName());
                    return new JsonResultWriter(result, 200);
                } catch (UserAlreadyInChatException e) {
                    result.put("isJoined", "false");
                    result.put("message", e.getMessage());
                    return new JsonResultWriter(result, 555);
                }
            } else {
                return authenticationRequiredErrorWriter();
            }
        }));
    }

    private void registerCreateChatGet() {
        handleGet("/api/chat/new", handlerRegistry);
    }

    private void registerCreateChatPost() {
        UrlMethodPair createChatPair = new UrlMethodPair("/api/chat/new", POST);
        handlerRegistry.register(createChatPair, ((request, response) -> {
            Optional<AuthenticationTokenDto> token = userService.checkAuthentication(
                    new AuthenticationTokenDto(request.getParameter("token"))
            );
            if (token.isPresent()) {
                JsonResult result = new JsonResult();
                try {
                    chatService.createChat(
                        request.getParameter("chatName"),
                        token.get().getUserId());
                    Collection<ChatDto> allChats = chatService.getAllChats();
                    List<String> chatList = new ArrayList<>();
                    allChats.forEach((chat) -> {
                        JsonResult res = new JsonResult();
                        res.put("chatId", chat.getChatId().value().toString());
                        res.put("chatName", chat.getChatName());
                        chatList.add(res.getResult());
                    });
                    result.put("chats", chatList.toArray(new String[chatList.size()]));
                    return new JsonResultWriter(result, 200);
                } catch (InvalidChatNameException e) {
                    result.put("message", e.getMessage());

                    return new JsonResultWriter(result, 555);
                }
            } else {
                return authenticationRequiredErrorWriter();
            }
        }));
    }

    private void registerChatListGet() {
        handleGet("/api/chats", handlerRegistry);
    }

    private void registerChatListPost() {
        UrlMethodPair postChatListRequest
                = new UrlMethodPair("/api/chats", POST);
        handlerRegistry.register(postChatListRequest, ((request, response) -> {
            Optional<AuthenticationTokenDto> token = userService.checkAuthentication(
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
                return authenticationRequiredErrorWriter();
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
