package entity;

import entity.tiny.chat.ChatId;
import entity.tiny.user.UserId;

import java.util.*;

public class Chat implements Entity<ChatId> {

    private Long id = 0L;
    private String chatName;
    private UserId creatorId;
    private final List<Message> messages = new ArrayList<>();
    private final Set<UserId> users = new HashSet<>();

    public Chat(String chatName, UserId creatorId) {
        this.chatName = chatName;
        this.creatorId = creatorId;
    }

    @Override
    public void setId(ChatId id) {
        this.id = id.value();
    }

    @Override
    public ChatId getId() {
        return new ChatId(id);
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public UserId getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UserId creatorId) {
        this.creatorId = creatorId;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public boolean addUser(UserId userId) {
        return users.add(userId);
    }

    public void removeAllMessages() {
        messages.clear();
    }

    public void removeUser(UserId userId) {
        users.remove(userId);
    }

    public Collection<Message> getAllMessages() {
        return messages;
    }

    public Collection<UserId> getAllUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        if (id != null ? !id.equals(chat.id) : chat.id != null) return false;
        return chatName != null ? chatName.equals(chat.chatName)
                : chat.chatName == null && (creatorId != null ? creatorId.equals(chat.creatorId)
                : chat.creatorId == null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (chatName != null ? chatName.hashCode() : 0);
        result = 31 * result + (creatorId != null ? creatorId.hashCode() : 0);
        return result;
    }
}
