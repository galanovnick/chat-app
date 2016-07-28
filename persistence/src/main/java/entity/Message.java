package entity;

import entity.tiny.chat.MessageId;
import entity.tiny.user.UserId;

public class Message implements Entity<MessageId> {

    private MessageId id = new MessageId(0L);
    private UserId userId;
    private String text;

    public Message(UserId userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    @Override
    public MessageId getId() {
        return id;
    }

    @Override
    public void setId(MessageId id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        if (userId != null ? !userId.equals(message.userId) : message.userId != null) return false;
        return text != null ? text.equals(message.text) : message.text == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
