package service;

import entity.Message;
import entity.tiny.chat.ChatId;
import entity.tiny.user.UserId;
import service.impl.dto.ChatDto;
import service.impl.dto.MessageDto;

import java.util.Collection;

public interface ChatService {

    ChatId createChat(String chatName, UserId creatorId)
            throws InvalidChatNameException;

    void removeChat(ChatId chatId);

    ChatDto getChat(ChatId chatId);

    Collection<ChatDto> getAllChats();

    void joinChat(ChatId chatId, UserId userId)
            throws UserAlreadyInChatException;

    void leaveChat(ChatId chatId, UserId userId);

    Collection<UserId> getChatUsers(ChatId chatId);

    void addMessage(ChatId chatId, Message message);

    Collection<MessageDto> getAllChatMessages(ChatId chatId);
}
