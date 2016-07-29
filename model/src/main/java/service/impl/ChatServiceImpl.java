package service.impl;

import entity.Chat;
import entity.Message;
import entity.tiny.chat.ChatId;
import entity.tiny.user.UserId;
import repository.ChatRepository;
import service.ChatService;
import service.InvalidChatNameException;
import service.UserAlreadyInChatException;
import service.impl.dto.ChatDto;
import service.impl.dto.MessageDto;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatServiceImpl implements ChatService {

    private static ChatService instance;

    private final ChatRepository chatRepository = ChatRepository.getInstance();

    @Override
    public ChatId createChat(String chatName, UserId creatorId) throws InvalidChatNameException {
        checkNotNull(chatName, "Chat name cannot be null");
        checkNotNull(creatorId, "Creator cannot be null");

        if (chatName.equals("")) {
            throw new InvalidChatNameException("Chat name cannot be empty.");
        }

        Pattern patter = Pattern.compile("^( +)");
        Matcher matcher = patter.matcher(chatName);

        if (matcher.find()) {
            throw new InvalidChatNameException("Chat name cannot start with whitespace.");
        }

        if (chatRepository.isChatExists(chatName)) {
            throw new InvalidChatNameException("Chat with such name already exists.");
        }

        return chatRepository.add(new Chat(chatName, creatorId));
    }

    @Override
    public void removeChat(ChatId chatId) {
        checkNotNull(chatId, "Chat id cannot be null.");
        chatRepository.delete(chatId);
    }

    @Override
    public ChatDto getChat(ChatId chatId) {
        checkNotNull(chatId, "Chat id cannot be null.");
        Optional<Chat> chat = chatRepository.findOne(chatId);
        if (chat.isPresent()) {
            return new ChatDto(chat.get().getId(), chat.get().getChatName(), chat.get().getCreatorId());
        }
        throw new IllegalStateException("Attempt to get chat by invalid id.");
    }

    @Override
    public Collection<ChatDto> getAllChats() {
        return chatRepository.findAll().stream()
                .map(chat -> new ChatDto(chat.getId() ,chat.getChatName(), chat.getCreatorId()))
                .collect(Collectors.toList());
    }

    @Override
    public void joinChat(ChatId chatId, UserId userId) throws UserAlreadyInChatException {
        checkNotNull(chatId, "Chat id cannot be null.");
        checkNotNull(userId, "User id cannot be null.");

        Optional<Chat> chat = chatRepository.findOne(chatId);
        if (chat.isPresent()) {
            if (!chat.get().addUser(userId)) {
                throw new UserAlreadyInChatException();
            }
            return;
        }
        throw new IllegalStateException("Attempt to get chat by invalid id.");
    }

    @Override
    public void leaveChat(ChatId chatId, UserId userId) {
        checkNotNull(chatId, "Chat id cannot be null.");
        checkNotNull(userId, "User id cannot be null.");

        Optional<Chat> chat = chatRepository.findOne(chatId);
        if (chat.isPresent()) {
            chat.get().removeUser(userId);
            return;
        }
        throw new IllegalStateException("Attempt to get chat by invalid id.");
    }

    @Override
    public Collection<UserId> getChatUsers(ChatId chatId) {
        checkNotNull(chatId, "Chat id cannot be null.");

        Optional<Chat> chat = chatRepository.findOne(chatId);
        if (chat.isPresent()) {
            return chat.get().getAllUsers();
        }
        throw new IllegalStateException("Attempt to get chat by invalid id.");
    }

    @Override
    public void addMessage(ChatId chatId, Message message) {
        checkNotNull(chatId, "Chat id cannot be null.");
        checkNotNull(message, "Message cannot be null.");

        Optional<Chat> chat = chatRepository.findOne(chatId);
        if (chat.isPresent()) {
            chat.get().addMessage(message);
            return;
        }
        throw new IllegalStateException("Attempt to get chat by invalid id.");
    }

    @Override
    public Collection<MessageDto> getAllChatMessages(ChatId chatId) {
        checkNotNull(chatId, "Chat id cannot be null.");

        Optional<Chat> chat = chatRepository.findOne(chatId);
        if (chat.isPresent()) {
            return chat.get().getAllMessages().stream()
                    .map(message -> new MessageDto(message.getText(), message.getUsername()))
                    .collect(Collectors.toList());
        }
        throw new IllegalStateException("Attempt to get chat by invalid id.");
    }

    public static ChatService getInstance() {
        if (instance == null) {
            return instance = new ChatServiceImpl();
        }
        return instance;
    }
}
