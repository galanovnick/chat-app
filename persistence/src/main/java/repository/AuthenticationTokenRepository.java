package repository;

import entity.AuthenticationToken;
import entity.tiny.user.AuthenticationTokenId;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class AuthenticationTokenRepository
        extends InMemoryRepository<AuthenticationToken, AuthenticationTokenId> {


    private static AuthenticationTokenRepository instance;

    private final AtomicLong idCounter = new AtomicLong(0L);

    @Override
    protected AuthenticationTokenId nextId() {
        return new AuthenticationTokenId(idCounter.getAndAdd(1L));
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
