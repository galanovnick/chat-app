package service.impl.dto;

import entity.tiny.user.UserId;

public class ChatDto {

    private String chatName;
    private UserId ownerId;

    public ChatDto(String chatName, UserId ownerId) {
        this.chatName = chatName;
        this.ownerId = ownerId;
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

        if (chatName != null ? !chatName.equals(chatDto.chatName) : chatDto.chatName != null) return false;
        return ownerId != null ? ownerId.equals(chatDto.ownerId) : chatDto.ownerId == null;

    }

    @Override
    public int hashCode() {
        int result = chatName != null ? chatName.hashCode() : 0;
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        return result;
    }
}
