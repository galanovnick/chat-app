package service;

public class UserAlreadyInChatException extends Exception {

    private final String message;

    public UserAlreadyInChatException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
