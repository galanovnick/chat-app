package repository;

import entity.User;

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

    public static UserRepository getInstance() {
        if (instance == null) {
            return instance = new UserRepository();
        }
        return instance;
    }
}
