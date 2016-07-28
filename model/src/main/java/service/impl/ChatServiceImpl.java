package service.impl;

import entity.AuthenticationToken;
import entity.Chat;
import entity.Message;
import entity.tiny.chat.ChatId;
import service.AuthenticationException;
import service.ChatService;

import java.util.Collection;

public class ChatServiceImpl implements ChatService {

    private static ChatService instance;

    @Override
    public ChatId createChat(AuthenticationToken token, String chatName) throws AuthenticationException {
        return null;
    }

    @Override
    public Collection<Chat> getAllChats(AuthenticationToken token) throws AuthenticationException {
        return null;
    }

    @Override
    public void joinChat(AuthenticationToken token, ChatId chatId) throws AuthenticationException {

    }

    @Override
    public void leaveChat(AuthenticationToken token, ChatId chatId) throws AuthenticationException {

    }

    @Override
    public void addMessage(AuthenticationToken token, String text, ChatId chatId) throws AuthenticationException {

    }

    @Override
    public Collection<Message> getAllChatMessages(AuthenticationToken token, ChatId chatId) throws AuthenticationException {
        return null;
    }

    public static ChatService getInstance() {
        if (instance == null) {
            return instance = new ChatServiceImpl();
        }
        return instance;
    }
}
