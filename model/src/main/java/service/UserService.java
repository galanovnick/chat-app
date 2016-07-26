package service;

import entity.AuthenticatedUser;
import entity.AuthenticationToken;
import entity.User;
import entity.tiny.UserName;
import entity.tiny.UserPassword;
import service.impl.dto.UserDto;

import java.util.Collection;

public interface UserService {

    long registerUser(UserDto user) throws InvalidUserDataException;

    boolean isUserRegistered(Long id);

    void deleteUser(Long id);

    Collection<User> getAllRegisteredUsers();

    void terminateAuthentication(AuthenticationToken token);

    Collection<AuthenticatedUser> getAllAuthenticatedUsers();

    AuthenticationToken login(UserName username, UserPassword password) throws AuthenticationException;

    boolean isUserAuthenticated(Long id, AuthenticationToken token);
}
