package repository;

import entity.Chat;
import entity.tiny.chat.ChatId;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

public class ChatRepository extends InMemoryRepository<Chat, ChatId> {

    private static ChatRepository instance;

    private AtomicLong idCounter = new AtomicLong(0L);

    public boolean isChatExists(String chatName) {
        for (Chat chat : entries.values()) {
            if (chat.getChatName().equals(chatName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected ChatId nextId() {
        return new ChatId(idCounter.getAndAdd(1));
    }

    public static ChatRepository getInstance() {
        if (instance == null) {
            return instance = new ChatRepository();
        }
        return instance;
    }
}
