package service.impl;

import entity.AuthenticationToken;
import entity.User;
import entity.tiny.UserName;
import entity.tiny.UserPassword;
import service.AuthenticationException;
import service.InvalidUserDataException;
import service.UserService;

public class UserServiceImpl implements UserService {

    private static UserServiceImpl instance;

    @Override
    public long registerUser(User user) throws InvalidUserDataException {
        return 0;
    }

    @Override
    public AuthenticationToken login(UserName username, UserPassword password)
            throws AuthenticationException {
        return null;
    }

    public static UserServiceImpl getInstance() {
        if (instance == null) {
            return instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean isUserRegistered(long id) {
        return false;
    }

    @Override
    public boolean isUserAuthenticated(long id, AuthenticationToken token) {
        return false;
    }

    private UserServiceImpl(){}
}
