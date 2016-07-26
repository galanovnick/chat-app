package repository;

import com.google.common.base.Optional;
import entity.AuthenticatedUser;
import entity.AuthenticationToken;

import static com.google.common.base.Preconditions.checkNotNull;

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

    public Optional<AuthenticatedUser> findByToken(AuthenticationToken token) {
        checkNotNull(token, "Token cannot be null.");

        for (AuthenticatedUser user : entries.values()) {
            if (user.getToken().equals(token)) {
                return Optional.of(user);
            }
        }
        return Optional.absent();
    }

    public static UserAuthenticationRepository getInstance() {
        if (instance == null) {
            return instance = new UserAuthenticationRepository();
        }
        return instance;
    }
}
