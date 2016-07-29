package service.impl.dto;

import entity.tiny.chat.ChatId;
import entity.tiny.user.UserId;

public class ChatDto {

    private ChatId chatId;
    private String chatName;
    private UserId ownerId;

    public ChatDto(ChatId chatId, String chatName, UserId ownerId) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.ownerId = ownerId;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public void setChatId(ChatId chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public UserId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UserId ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatDto chatDto = (ChatDto) o;

        if (chatId != null ? !chatId.equals(chatDto.chatId) : chatDto.chatId != null) return false;
        return chatName != null ? chatName.equals(chatDto.chatName)
                : chatDto.chatName == null && (ownerId != null ? ownerId.equals(chatDto.ownerId)
                : chatDto.ownerId == null);

    }

    @Override
    public int hashCode() {
        int result = chatId != null ? chatId.hashCode() : 0;
        result = 31 * result + (chatName != null ? chatName.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        return result;
    }
}
