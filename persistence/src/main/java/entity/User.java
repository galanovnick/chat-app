package entity;

import entity.tiny.user.UserId;
import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;

public class User implements Entity<UserId> {

    private Long id = 0L;
    private String username;
    private String password;

    public User(UserName username, UserPassword password) {
        this.username = username.value();
        this.password = password.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        return password != null ? password.equals(user.password) : user.password == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public UserId getId() {
        return new UserId(id);
    }

    @Override
    public void setId(UserId id) {
        this.id = id.value();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(UserName username) {
        this.username = username.value();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(UserPassword password) {
        this.password = password.value();
    }
}
