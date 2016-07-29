package service;

import entity.AuthenticationToken;
import entity.tiny.user.UserId;
import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import service.impl.dto.AuthenticationTokenDto;
import service.impl.dto.RegistrationDto;
import service.impl.dto.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    UserId registerUser(RegistrationDto user) throws InvalidUserDataException;

    UserDto getUser(UserId userId);

    boolean isUserRegistered(UserId id);

    void deleteUser(UserId id);

    Collection<UserDto> getAllRegisteredUsers();

    void terminateAuthentication(AuthenticationTokenDto user);

    Collection<AuthenticationTokenDto> getAllAuthenticatedUsers();

    AuthenticationTokenDto login(UserName username, UserPassword password) throws AuthenticationException;

    Optional<AuthenticationToken> checkAuthentication(AuthenticationTokenDto token);
}
