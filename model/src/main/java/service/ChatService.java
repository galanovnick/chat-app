package service;

import entity.Message;
import entity.tiny.chat.ChatId;
import entity.tiny.user.UserId;
import service.impl.dto.ChatDto;
import service.impl.dto.MessageDto;
import service.impl.dto.UserDto;

import java.util.Collection;

public interface ChatService {

    ChatId createChat(String chatName, UserDto creator)
            throws InvalidChatNameException;

    void removeChat(ChatId chatId);

    ChatDto getChat(ChatId chatId);

    Collection<ChatDto> getAllChats();

    void joinChat(ChatId chatId, UserId userId)
            throws UserAlreadyInChatException;

    void leaveChat(ChatId chatId, UserId userId);

    Collection<UserDto> getChatUsers(ChatId chatId);

    void addMessage(ChatId chatId, Message message);

    Collection<MessageDto> getAllChatMessages(ChatId chatId);
}
