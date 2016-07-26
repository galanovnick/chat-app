package entity.tiny;

public class UserName {

    private final String username;

    public UserName(String username) {
        this.username = username;
    }

    public String value() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserName userName = (UserName) o;

        return username != null ? username.equals(userName.username) : userName.username == null;

    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}
