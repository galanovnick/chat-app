package entity;

import entity.tiny.AuthenticationTokenId;

public class AuthenticationToken implements Entity<AuthenticationTokenId>{

    private Long id;
    private String token;
    private Long userId;

    public AuthenticationToken(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public AuthenticationToken(Long id, String token, Long userId) {
        this.id = id;
        this.token = token;
        this.userId = userId;
    }

    @Override
    public AuthenticationTokenId getId() {
        return new AuthenticationTokenId(id);
    }

    public void setId(AuthenticationTokenId id) {
        this.id = id.value();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
