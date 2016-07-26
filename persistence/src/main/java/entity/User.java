package entity;

import entity.tiny.UserName;
import entity.tiny.UserPassword;

public class User implements Entry<Long> {

    private Long id;
    private UserName username;
    private UserPassword password;

    public User(Long id, UserName username, UserPassword password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserName getUsername() {
        return username;
    }

    public void setUsername(UserName username) {
        this.username = username;
    }

    public UserPassword getPassword() {
        return password;
    }

    public void setPassword(UserPassword password) {
        this.password = password;
    }
}
