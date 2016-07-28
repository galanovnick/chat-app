package service;

import entity.AuthenticationToken;
import entity.Chat;
import entity.Message;
import entity.tiny.chat.ChatId;

import java.util.Collection;

public interface ChatService {

    ChatId createChat(AuthenticationToken token, String chatName)
            throws AuthenticationException;

    Collection<Chat> getAllChats(AuthenticationToken token)
        throws AuthenticationException;

    void joinChat(AuthenticationToken token, ChatId chatId)
        throws AuthenticationException;
    //TODO: user already in chat/ user already left exception???
    void leaveChat(AuthenticationToken token, ChatId chatId)
        throws AuthenticationException;

    void addMessage(AuthenticationToken token, String text, ChatId chatId)
        throws AuthenticationException;

    Collection<Message> getAllChatMessages(AuthenticationToken token, ChatId chatId)
        throws AuthenticationException;
}
