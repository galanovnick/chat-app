package service;

import entity.Message;
import entity.tiny.chat.ChatId;
import entity.tiny.chat.MessageId;
import entity.tiny.user.UserId;
import entity.tiny.user.UserName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.impl.ChatServiceImpl;
import service.impl.dto.MessageDto;
import service.impl.dto.UserDto;

import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ChatServiceShould {

    private final ChatService chatService = ChatServiceImpl.getInstance();

    private ChatId chatId;
    private String chatName = UUID.randomUUID().toString();

    @Before
    public void before() {
        if (chatService.getAllChats().size() > 0) {
            fail("Repository have to be empty.");
        }
        try {
            chatId = chatService.createChat(
                    chatName,
                    new UserDto(new UserName("some-user")));
        } catch (InvalidChatNameException e) {
            e.printStackTrace();
            fail("Failed on chat creation.");
        }
    }

    @After
    public void after() {
        chatService.removeChat(chatId);
    }

    @Test
    public void createChat() {
        assertEquals("Failed on chat creation.", chatName,
                chatService.getChat(chatId).getChatName());
    }

    @Test(expected = InvalidChatNameException.class)
    public void notCreateChatWithDuplicatedName() throws InvalidChatNameException {
        chatService.createChat(chatName, new UserDto(new UserName("some-user")));
        fail("Failed on chat with duplicated name creation.");
    }

    @Test(expected = InvalidChatNameException.class)
    public void notCreateChatWithEmptyName() throws InvalidChatNameException {
        chatService.createChat("", new UserDto(new UserName("some-user")));
        fail("Failed on chat with empty name creation.");
    }

    @Test(expected = InvalidChatNameException.class)
    public void notCreateChatWithInvalidName() throws InvalidChatNameException {
        chatService.createChat("   ", new UserDto(new UserName("some-user")));
        fail("Failed on chat with invalid name creation.");
    }

    @Test
    public void addUserToChat() {

        final Collection<UserDto> chatUsers = chatService.getChatUsers(chatId);

        if (chatUsers.size() > 0) {
            fail("Repository have to be empty.");
        }

        try {
            chatService.joinChat(chatId, new UserId(0L));
        } catch (UserAlreadyInChatException e) {
            e.printStackTrace();
            fail("Failed on user addition.");
        }

        final int userListSize = chatUsers.size();
        chatService.leaveChat(chatId, new UserId(0L));

        assertEquals("Failed on user addition.", 1,
                userListSize);
    }

    @Test(expected = UserAlreadyInChatException.class)
    public void notAddDuplicatedUserToChat() throws UserAlreadyInChatException {

        final Collection<UserDto> chatUsers = chatService.getChatUsers(chatId);

        if (chatUsers.size() > 0) {
            fail("Repository have to be empty.");
        }

        chatService.joinChat(chatId, new UserId(0L));
        chatService.joinChat(chatId, new UserId(0L));
        fail("Failed on duplicated user addition.");
    }

    @Test
    public void removeUsersFromChat() {
        final Collection<UserDto> chatUsers = chatService.getChatUsers(chatId);

        if (chatUsers.size() > 0) {
            fail("Repository have to be empty.");
        }

        try {
            chatService.joinChat(chatId, new UserId(0L));
            chatService.leaveChat(chatId, new UserId(0L));
        } catch (UserAlreadyInChatException e) {
            e.printStackTrace();
            fail("Failed on user removal.");
        }

        assertEquals("Failed on user removal.", 0,
                chatUsers.size());
    }

    @Test
    public void addMessageToChat() {
        final Collection<MessageDto> chatMessages = chatService.getAllChatMessages(chatId);

        if (chatMessages.size() > 0) {
            fail("Repository have to be empty.");
        }

        chatService.addMessage(chatId, new Message(new UserId(0L), "Hi!"));

        assertEquals("Failed on message addition.", 1,
                chatService.getAllChatMessages(chatId).size());
    }
}
