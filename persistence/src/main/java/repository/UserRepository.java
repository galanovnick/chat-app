package repository;

import com.google.common.base.Optional;
import entity.User;
import entity.tiny.UserName;

public class UserRepository extends InMemoryRepository<User, Long> {

    private static UserRepository instance;

    private final static Object ID_LOCK = new Object();

    private long idCounter = 0;

    @Override
    protected Long nextId() {
        synchronized (ID_LOCK) {
            return idCounter++;
        }
    }

    private UserRepository() {}

    public Optional<User> getUserByUsername(UserName userName) {
        for (User user : entries.values()) {
            if (user.getUsername().equals(userName)) {
                return Optional.of(user);
            }
        }
        return Optional.absent();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            return instance = new UserRepository();
        }
        return instance;
    }
}
