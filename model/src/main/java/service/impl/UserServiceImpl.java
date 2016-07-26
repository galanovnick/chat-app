package service.impl;

import com.google.common.base.Optional;
import entity.AuthenticatedUser;
import entity.AuthenticationToken;
import entity.User;
import entity.tiny.UserName;
import entity.tiny.UserPassword;
import repository.UserAuthenticationRepository;
import repository.UserRepository;
import service.AuthenticationException;
import service.InvalidUserDataException;
import service.UserService;
import service.impl.dto.UserDto;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class UserServiceImpl implements UserService {

    private static UserServiceImpl instance;

    private final UserRepository userRepository = UserRepository.getInstance();
    private final UserAuthenticationRepository userAuthenticationRepository
            = UserAuthenticationRepository.getInstance();

    @Override
    public long registerUser(UserDto user) throws InvalidUserDataException {
        checkNotNull(user, "User dto cannot be null.");
        if (user.getUsername().equals("")
                || user.getPassword().equals("")
                || user.getPasswordConfirm().equals("")) {
            throw new InvalidUserDataException("Fields cannot be empty.");
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            throw new InvalidUserDataException("Password do not match.");
        }
        if (userRepository.getUserByUsername(new UserName(user.getUsername())).isPresent()) {
            throw new InvalidUserDataException("User with such name already exists.");
        }
        return userRepository.insert(
                new User(new UserName(user.getUsername()),
                        new UserPassword(user.getPassword())));
    }

    @Override
    public AuthenticationToken login(UserName username, UserPassword password)
            throws AuthenticationException {

        checkNotNull(username, "Username cannot be null");
        checkNotNull(password, "Password cannot be null");

        Optional<User> user = userRepository.getUserByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            AuthenticationToken token = new AuthenticationToken(username.getUsername());
            AuthenticatedUser authenticatedUser = new AuthenticatedUser(token, user.get().getId());
            userAuthenticationRepository.insert(authenticatedUser);
            return token;
        }

        throw new AuthenticationException();
    }

    public static UserServiceImpl getInstance() {
        if (instance == null) {
            return instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean isUserRegistered(Long id) {
        checkArgument(id > -1, "Id cannot be negative.");

        return userRepository.findOne(id).isPresent();
    }

    @Override
    public boolean isUserAuthenticated(Long userId, AuthenticationToken token) {

        checkArgument(userId > -1, "Id cannot be negative.");
        checkNotNull(token, "Token cannot be null");

        Optional<AuthenticatedUser> user = userAuthenticationRepository.findByToken(token);

        if (user.isPresent() && user.get().getUserId().equals(userId)) {
            return true;
        }
        return false;
    }

    private UserServiceImpl(){}
}
