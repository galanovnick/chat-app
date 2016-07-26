package service;

import entity.AuthenticationToken;
import entity.tiny.UserName;
import entity.tiny.UserPassword;
import org.junit.Before;
import org.junit.Test;
import service.impl.UserServiceImpl;
import service.impl.dto.UserDto;

import static org.junit.Assert.assertEquals;

public class UserServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();

    @Test
    public void registerUser() throws InvalidUserDataException {

        final UserDto user = new UserDto("vasya", "123", "123");

        final Long userId = userService.registerUser(user);

        assertEquals("Failed user registration.", true, userService.isUserRegistered(userId));
    }

    @Test
    public void authenticateUser() throws AuthenticationException, InvalidUserDataException {
        final UserDto user = new UserDto("petya", "123", "123");

        final Long userId = userService.registerUser(user);

        final AuthenticationToken token = userService.login(
                new UserName(user.getUsername()),
                new UserPassword(user.getPassword()));

        assertEquals("Failed user authentication",
                true, userService.isUserAuthenticated(userId, token));
    }
}
