package entity;

import entity.tiny.chat.ChatId;

public class Chat implements Entity<ChatId> {

    private Long id;
    private String chatName;

    public Chat(ChatId id, String chatName) {
        this.id = id.value();
        this.chatName = chatName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        if (id != null ? !id.equals(chat.id) : chat.id != null) return false;
        return chatName != null ? chatName.equals(chat.chatName) : chat.chatName == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (chatName != null ? chatName.hashCode() : 0);
        return result;
    }
}
