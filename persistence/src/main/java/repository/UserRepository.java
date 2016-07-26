package repository;

import entity.User;
import entity.tiny.UserId;
import entity.tiny.UserName;

import java.util.Optional;

public class UserRepository extends InMemoryRepository<User, UserId> {

    private static UserRepository instance;

    private final static Object ID_LOCK = new Object();

    private long idCounter = 0;

    @Override
    protected UserId nextId() {
        synchronized (ID_LOCK) {
            return new UserId(idCounter++);
        }
    }

    private UserRepository() {}

    public Optional<User> getUserByUsername(UserName userName) {
        for (User user : entries.values()) {
            if (user.getUsername().equals(userName.value())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            return instance = new UserRepository();
        }
        return instance;
    }
}
