package entity;

import entity.tiny.chat.MessageId;

public class Message implements Entity<MessageId> {

    private MessageId id;
    private String text;

    public Message(MessageId id, String text) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        return text != null ? text.equals(message.text) : message.text == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
