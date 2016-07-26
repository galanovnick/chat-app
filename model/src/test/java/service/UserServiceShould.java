package service;

import entity.AuthenticationToken;
import entity.User;
import entity.tiny.UserName;
import entity.tiny.UserPassword;
import org.junit.Before;
import org.junit.Test;
import service.impl.UserServiceImpl;

import static org.junit.Assert.assertEquals;

public class UserServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();

    private final User user = new User(0L, new UserName("vasya"), new UserPassword("123"));

    @Before
    public void before() throws InvalidUserDataException {
        userService.registerUser(user);
    }

    @Test
    public void registerUser() {
        assertEquals("Failed user registration.", true, userService.isUserRegistered(user.getId()));
    }

    @Test
    public void authenticateUser() throws AuthenticationException {
        AuthenticationToken token = userService.login(user.getUsername(), user.getPassword());
        assertEquals("", true, userService.isUserAuthenticated(user.getId(), token));
    }
}
