package service.impl;

import entity.Message;
import entity.tiny.chat.ChatId;
import entity.tiny.user.UserId;
import service.ChatService;
import service.InvalidChatNameException;
import service.UserAlreadyInChatException;
import service.impl.dto.ChatDto;
import service.impl.dto.MessageDto;
import service.impl.dto.UserDto;

import java.util.Collection;

public class ChatServiceImpl implements ChatService {

    private static ChatService instance;

    @Override
    public ChatId createChat(String chatName, UserDto creator) throws InvalidChatNameException {
        return null;
    }

    @Override
    public void removeChat(ChatId chatId) {

    }

    @Override
    public ChatDto getChat(ChatId chatId) {
        return null;
    }

    @Override
    public Collection<ChatDto> getAllChats() {
        return null;
    }

    @Override
    public void joinChat(ChatId chatId, UserId userId) throws UserAlreadyInChatException {

    }

    @Override
    public void leaveChat(ChatId chatId, UserId userId) {

    }

    @Override
    public Collection<UserDto> getChatUsers(ChatId chatId) {
        return null;
    }

    @Override
    public void addMessage(ChatId chatId, Message message) {

    }

    @Override
    public Collection<MessageDto> getAllChatMessages(ChatId chatId) {
        return null;
    }

    public static ChatService getInstance() {
        if (instance == null) {
            return instance = new ChatServiceImpl();
        }
        return instance;
    }
}
