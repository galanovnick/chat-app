package service;

import entity.AuthenticationToken;
import entity.User;

public interface UserService {

    long registerUser(User user) throws InvalidUserDataException;

    AuthenticationToken login(String username, String password) throws AuthenticationException;
}
