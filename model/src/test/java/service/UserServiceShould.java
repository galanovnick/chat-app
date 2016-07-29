package service;

import entity.tiny.user.UserId;
import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.impl.UserServiceImpl;
import service.impl.dto.AuthenticationTokenDto;
import service.impl.dto.RegistrationDto;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UserServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();

    private final RegistrationDto user = new RegistrationDto(
            new UserName("Vasya"),
            new UserPassword("123"),
            new UserPassword("123"));

    private UserId userId;

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
    public void registerUser() {
        assertTrue("Failed user registration.", userService.isUserRegistered(userId));
    }

    @Test
    public void authenticateUser() {
        AuthenticationTokenDto token = null;
        try {
            token = userService.login(
                    new UserName(this.user.getUsername()),
                    new UserPassword(this.user.getPassword()));
        } catch (AuthenticationException e) {
            fail("Failed user addition.");
        }

        boolean actual = userService.checkAuthentication(token).isPresent();

        userService.terminateAuthentication(token);

        assertTrue("Failed user authentication", actual);
    }

    @Test(expected = InvalidUserDataException.class)
    public void notRegisterDuplicatedUser() throws InvalidUserDataException {

        userService.registerUser(user);

        fail("Expected InvalidUserDataException.");
    }

    @Test(expected = InvalidUserDataException.class)
    public void notRegisterUserWithDifferentPasswords() throws InvalidUserDataException {
        final RegistrationDto user = new RegistrationDto(
                new UserName("Vasya"),
                new UserPassword("123"),
                new UserPassword("321"));

        userService.registerUser(user);

        fail("Expected InvalidUserDataException.");
    }

    @Test(expected = AuthenticationException.class)
    public void notAuthenticateInvalidUser() throws AuthenticationException {
        userService.login(
                new UserName(UUID.randomUUID().toString()),
                new UserPassword(UUID.randomUUID().toString()));

        fail("Expected AuthenticationException.");
    }
}
