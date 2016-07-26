package repository;

import entity.AuthenticatedUser;

public class UserAuthenticationRepository extends InMemoryRepository<AuthenticatedUser, Long> {

    private static UserAuthenticationRepository instance;

    private final static Object ID_LOCK = new Object();

    private long idCounter = 0;

    @Override
    protected Long nextId() {
        synchronized (ID_LOCK) {
            return idCounter++;
        }
    }

    private UserAuthenticationRepository() {}

    public static UserAuthenticationRepository getInstance() {
        if (instance == null) {
            return instance = new UserAuthenticationRepository();
        }
        return instance;
    }
}
