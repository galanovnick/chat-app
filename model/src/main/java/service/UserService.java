package service;

import entity.AuthenticationToken;
import entity.tiny.user.UserId;
import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import service.impl.dto.RegistrationDto;
import service.impl.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserId registerUser(RegistrationDto user) throws InvalidUserDataException;

    boolean isUserRegistered(UserId id);

    void deleteUser(UserId id);

    Collection<UserDto> getAllRegisteredUsers();

    void terminateAuthentication(AuthenticationToken token);

    Collection<AuthenticationToken> getAllAuthenticatedUsers();

    AuthenticationToken login(UserName username, UserPassword password) throws AuthenticationException;

    boolean isUserAuthenticated(UserId userId, String token);
}
