package service;

public class InvalidChatNameException extends Exception {

    private String message;

    public InvalidChatNameException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
