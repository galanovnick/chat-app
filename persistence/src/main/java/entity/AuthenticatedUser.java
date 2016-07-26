package entity;

import entity.tiny.UserName;

public class AuthenticatedUser implements Entry<Long> {

    private Long id;
    private AuthenticationToken token;
    private UserName username;

    public AuthenticatedUser(Long id, AuthenticationToken token, UserName username) {
        this.id = id;
        this.token = token;
        this.username = username;
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

    public UserName getUsername() {
        return username;
    }

    public void setUsername(UserName username) {
        this.username = username;
    }
}
