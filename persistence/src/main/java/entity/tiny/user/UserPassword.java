package entity.tiny.user;

public class UserPassword {

    private final String password;

    public UserPassword(String password) {
        this.password = password;
    }

    public String value() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPassword that = (UserPassword) o;

        return password != null ? password.equals(that.password) : that.password == null;

    }

    @Override
    public int hashCode() {
        return password != null ? password.hashCode() : 0;
    }
}
