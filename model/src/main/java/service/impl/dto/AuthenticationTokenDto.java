package service.impl.dto;

import entity.tiny.user.UserId;

public class AuthenticationTokenDto {

    private String token;
    private UserId userId = new UserId(-1L);

    public AuthenticationTokenDto(String token) {
        this.token = token;
    }

    public AuthenticationTokenDto(String token, UserId userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthenticationTokenDto that = (AuthenticationTokenDto) o;

        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;

    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
