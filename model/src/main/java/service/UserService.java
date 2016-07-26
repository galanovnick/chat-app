package service;

import entity.AuthenticationToken;
import entity.tiny.UserName;
import entity.tiny.UserPassword;
import service.impl.dto.UserDto;

public interface UserService {

    long registerUser(UserDto user) throws InvalidUserDataException;

    boolean isUserRegistered(Long id);

    AuthenticationToken login(UserName username, UserPassword password) throws AuthenticationException;

    boolean isUserAuthenticated(Long id, AuthenticationToken token);
}
