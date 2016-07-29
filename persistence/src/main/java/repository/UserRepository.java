package repository;

import entity.User;
import entity.tiny.user.UserId;
import entity.tiny.user.UserName;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository extends InMemoryRepository<User, UserId> {

    private static UserRepository instance;

    private final AtomicLong idCounter = new AtomicLong(0L);

    @Override
    protected UserId nextId() {
        return new UserId(idCounter.getAndAdd(1L));
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
