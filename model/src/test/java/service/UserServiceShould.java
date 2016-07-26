package service;

import entity.AuthenticationToken;
import entity.tiny.UserName;
import entity.tiny.UserPassword;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.impl.UserServiceImpl;
import service.impl.dto.UserDto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();

    private final UserDto user = new UserDto("vasya", "123", "123");

    private Long userId;

    @Before
    public void before() throws InvalidUserDataException {
        if (userService.getAllAuthenticatedUsers().size() > 0
                || userService.getAllRegisteredUsers().size() > 0) {
            fail("Repositories have to be empty.");
        }
        userId = userService.registerUser(user);
    }

    @After
    public void after() {
        userService.deleteUser(userId);
    }

    @Test
    public void registerUser() throws InvalidUserDataException {
        assertEquals("Failed user registration.", true, userService.isUserRegistered(userId));
    }

    @Test
    public void authenticateUser() throws AuthenticationException, InvalidUserDataException {
        final AuthenticationToken token = userService.login(
                new UserName(user.getUsername()),
                new UserPassword(user.getPassword()));

        boolean actual = userService.isUserAuthenticated(userId, token);

        userService.terminateAuthentication(token);

        assertEquals("Failed user authentication", true, actual);
    }

    @Test(expected = InvalidUserDataException.class)
    public void notRegisterDuplicatedUser() throws InvalidUserDataException {

        userService.registerUser(user);

        fail("Expected InvalidUserDataException.");
    }

    @Test(expected = InvalidUserDataException.class)
    public void notRegisterUserWithDifferentPasswords() throws InvalidUserDataException {
        final UserDto user = new UserDto("user", "123", "321");

        userService.registerUser(user);

        fail("Expected InvalidUserDataException.");
    }
}
