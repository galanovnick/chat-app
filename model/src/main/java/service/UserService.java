package service;

import entity.AuthenticationToken;
import entity.User;
import entity.tiny.UserName;
import entity.tiny.UserPassword;

public interface UserService {

    long registerUser(User user) throws InvalidUserDataException;

    boolean isUserRegistered(long id);

    AuthenticationToken login(UserName username, UserPassword password) throws AuthenticationException;

    boolean isUserAuthenticated(long id, AuthenticationToken token);
}
