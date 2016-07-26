package entity;

public class AuthenticatedUser implements Entry<Long> {

    private Long id = 0L;
    private AuthenticationToken token;
    private Long userId;

    public AuthenticatedUser(AuthenticationToken token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public AuthenticationToken getToken() {
        return token;
    }

    public void setToken(AuthenticationToken token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
