package entity.tiny.chat;

import entity.tiny.EntityId;

public class ChatId implements EntityId<Long> {
    private final Long id;

    public ChatId(Long id) {
        this.id = id;
    }

    public Long value() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatId chatId = (ChatId) o;

        return id != null ? id.equals(chatId.id) : chatId.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
