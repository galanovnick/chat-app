package entity;

import entity.tiny.chat.MessageId;

public class Message implements Entity<MessageId> {

    private MessageId id = new MessageId(0L);
    private String username;
    private String text;

    public Message(String username, String text) {
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        if (username != null ? !username.equals(message.username) : message.username != null) return false;
        return text != null ? text.equals(message.text) : message.text == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
