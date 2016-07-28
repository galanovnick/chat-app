package service.impl.dto;

import entity.tiny.user.UserName;

public class UserDto {

    private String username;

    public UserDto(UserName username) {
        this.username = username.value();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(UserName username) {
        this.username = username.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDto userDto = (UserDto) o;

        return username != null ? username.equals(userDto.username) : userDto.username == null;

    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}
