package service.impl.dto;

import entity.tiny.UserName;
import entity.tiny.UserPassword;

public class RegistrationDto {

    private String username;
    private String password;
    private String passwordConfirm;

    public RegistrationDto(UserName username, UserPassword password, UserPassword passwordConfirm) {
        this.username = username.value();
        this.password = password.value();
        this.passwordConfirm = passwordConfirm.value();
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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(UserPassword passwordConfirm) {
        this.passwordConfirm = passwordConfirm.value();
    }
}
