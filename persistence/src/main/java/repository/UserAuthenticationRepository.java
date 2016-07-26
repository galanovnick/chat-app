package repository;

import com.google.common.base.Optional;
import com.sun.javaws.exceptions.InvalidArgumentException;
import entity.AuthenticatedUser;
import entity.AuthenticationToken;

import java.util.Iterator;

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

    public void deleteByToken(AuthenticationToken token) {
        checkNotNull(token, "Token cannot be null.");

        boolean isRemoved = false;
        Iterator<AuthenticatedUser> iter = entries.values().iterator();
        while(iter.hasNext()) {
            if (iter.next().getToken().equals(token)) {
                iter.remove();
                isRemoved = true;
                break;
            }
        }
        if (!isRemoved) {
            throw new IllegalArgumentException("User with token = '"
                    + token.getToken() + "' does not authenticated.");
        }
    }

    public static UserAuthenticationRepository getInstance() {
        if (instance == null) {
            return instance = new UserAuthenticationRepository();
        }
        return instance;
    }
}
