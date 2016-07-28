package service;

public class UserAlreadyInChatException extends Exception {

    public UserAlreadyInChatException() {
    }

    public UserAlreadyInChatException(String message) {
        super(message);
    }
}
