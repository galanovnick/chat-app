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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static controller.ControllerConstants.*;
import static controller.HttpRequestMethod.POST;
import static javax.servlet.http.HttpServletResponse.SC_OK;

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
                    new AuthenticationTokenDto(request.getParameter(TOKEN_PARAMETER))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();

                Collection<MessageDto> allChatMessages =
                        chatService.getAllChatMessages(
                                new ChatId(Long.parseLong(request.getParameter(CHAT_ID_PARAMETER))));
                List<String> messageList = new ArrayList<>();
                allChatMessages.forEach((message) -> {
                    JsonResult messageResult = new JsonResult();
                    messageResult.put("text", message.getText());
                    messageResult.put("username", message.getAuthorName());
                    messageList.add(messageResult.getResult());
                });
                result.put("messages", messageList.toArray(new String[messageList.size()]));
                return new JsonResultWriter(result, SC_OK);
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
                    new AuthenticationTokenDto(request.getParameter(TOKEN_PARAMETER))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();
                Long chatId = Long.parseLong(request.getParameter(CHAT_ID_PARAMETER));
                chatService.addMessage(
                        new ChatId(chatId),
                        new Message(
                                userService.getUser(token.get().getUserId()).getUsername(),
                                request.getParameter(MESSAGE_PARAMETER))
                );
                result.put("roomId", chatId.toString());
                Collection<MessageDto> allChatMessages =
                        chatService.getAllChatMessages(new ChatId(chatId));
                List<String> messageList = new ArrayList<>();
                allChatMessages.forEach((message) -> {
                    JsonResult messageResult = new JsonResult();
                    messageResult.put("text", message.getText());
                    messageResult.put(USERNAME_PARAMETER, message.getAuthorName());
                    messageList.add(messageResult.getResult());
                });
                result.put("messages", messageList.toArray(new String[messageList.size()]));
                return new JsonResultWriter(result, SC_OK);
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
                    new AuthenticationTokenDto(request.getParameter(TOKEN_PARAMETER))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();

                chatService.leaveChat(
                        new ChatId(Long.parseLong(request.getParameter(CHAT_ID_PARAMETER))),
                        token.get().getUserId()
                );
                result.put(MESSAGE_PARAMETER, "User successfully left.");
                return new JsonResultWriter(result, SC_OK);
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
                    result.put("chatId", chatId.toString());
                    result.put("chatName",
                            chatService.getChat(new ChatId(chatId)).getChatName());
                    return new JsonResultWriter(result, 200);
                } catch (UserAlreadyInChatException e) {
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
                    new AuthenticationTokenDto(request.getParameter(TOKEN_PARAMETER))
            );
            if (token.isPresent()) {
                JsonResult result = new JsonResult();
                try {
                    ChatId chatId = chatService.createChat(
                            request.getParameter(CHAT_NAME_PARAMETER),
                            token.get().getUserId());
                    Collection<ChatDto> allChats = chatService.getAllChats();
                    List<String> chatList = new ArrayList<>();
                    result.put(CHAT_ID_PARAMETER, chatId.value().toString());
                    allChats.forEach((chat) -> {
                        JsonResult res = new JsonResult();
                        res.put(CHAT_ID_PARAMETER, chat.getChatId().value().toString());
                        res.put(CHAT_NAME_PARAMETER, chat.getChatName());
                        chatList.add(res.getResult());
                    });
                    result.put(CHATS_PARAMETER, chatList.toArray(new String[chatList.size()]));
                    return new JsonResultWriter(result, SC_OK);
                } catch (InvalidChatNameException e) {
                    result.put(MESSAGE_PARAMETER, e.getMessage());

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
                    new AuthenticationTokenDto(request.getParameter(TOKEN_PARAMETER))
            );

            if (token.isPresent()) {
                JsonResult result = new JsonResult();

                Collection<ChatDto> allChats = chatService.getAllChats();
                List<String> chatsList = new ArrayList<>();
                allChats.forEach((chat) -> {
                    JsonResult chats = new JsonResult();
                    chats.put(CHAT_ID_PARAMETER, chat.getChatId().value().toString());
                    chats.put(CHAT_NAME_PARAMETER, chat.getChatName());
                    chatsList.add(chats.getResult());
                });
                result.put(CHATS_PARAMETER, chatsList.toArray(new String[chatsList.size()]));
                return new JsonResultWriter(result, SC_OK);
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
