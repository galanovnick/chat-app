package entity.tiny.chat;

import entity.tiny.EntityId;

public class MessageId implements EntityId<Long> {

    private final Long id;

    public MessageId(Long id) {
        this.id = id;
    }

    @Override
    public Long value() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageId messageId = (MessageId) o;

        return id != null ? id.equals(messageId.id) : messageId.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
