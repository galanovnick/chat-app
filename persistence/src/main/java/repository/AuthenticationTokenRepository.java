package repository;

import entity.AuthenticationToken;
import entity.tiny.AuthenticationTokenId;

import java.util.Optional;

public class AuthenticationTokenRepository
        extends InMemoryRepository<AuthenticationToken, AuthenticationTokenId> {

    private static final Object ID_LOCK = new Object();

    private static AuthenticationTokenRepository instance;

    private long idCounter = 0;

    @Override
    protected AuthenticationTokenId nextId() {
        synchronized (ID_LOCK) {
            return new AuthenticationTokenId(idCounter++);
        }
    }

    private AuthenticationTokenRepository(){}

    public Optional<AuthenticationToken> findByTokenString(String tokenString) {
        for (AuthenticationToken token : entries.values()) {
            if (token.getToken().equals(tokenString)) {
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }

    public static AuthenticationTokenRepository getInstance() {
        if (instance == null) {
            return instance = new AuthenticationTokenRepository();
        }
        return instance;
    }
}
