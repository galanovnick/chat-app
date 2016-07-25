package persistence;

import persistence.entity.AuthenticationToken;
import persistence.entity.User;
import persistence.entity.tiny.UserId;

public interface UserService {

    UserId registerUser(User user) throws InvalidUserDataException;

    AuthenticationToken login(String username, String password) throws AuthenticationException;
}
