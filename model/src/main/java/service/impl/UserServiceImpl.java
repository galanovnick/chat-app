package service.impl;

import com.google.common.base.Optional;
import entity.AuthenticatedUser;
import entity.AuthenticationToken;
import entity.User;
import entity.tiny.UserName;
import entity.tiny.UserPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.UserAuthenticationRepository;
import repository.UserRepository;
import service.AuthenticationException;
import service.InvalidUserDataException;
import service.UserService;
import service.impl.dto.UserDto;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static UserServiceImpl instance;

    private final UserRepository userRepository = UserRepository.getInstance();
    private final UserAuthenticationRepository userAuthenticationRepository
            = UserAuthenticationRepository.getInstance();

    @Override
    public long registerUser(UserDto user) throws InvalidUserDataException {
        checkNotNull(user, "User dto cannot be null.");

        if (log.isDebugEnabled()) {
            log.debug("Trying to register user with name ='" + user.getUsername() + "'...");
        }

        if (user.getUsername().equals("")
                || user.getPassword().equals("")
                || user.getPasswordConfirm().equals("")) {
            if (log.isDebugEnabled()) {
                log.debug("Failed attempt to register user with name = '" + user.getUsername()
                        + "'. Reason: empty fields.");
            }
            throw new InvalidUserDataException("Fields cannot be empty.");
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            if (log.isDebugEnabled()) {
                log.debug("Failed attempt to register user with name = '" + user.getUsername()
                        + "'. Reason: different passwords.");
            }
            throw new InvalidUserDataException("Password do not match.");
        }
        if (userRepository.getUserByUsername(new UserName(user.getUsername())).isPresent()) {
            if (log.isDebugEnabled()) {
                log.debug("Failed attempt to register user with name = '" + user.getUsername()
                        + "'. Reason: user already exists.");
            }
            throw new InvalidUserDataException("User with such name already exists.");
        }
        final Long resId = userRepository.insert(
                new User(new UserName(user.getUsername()),
                        new UserPassword(user.getPassword())));

        if (log.isDebugEnabled()) {
            log.debug("User with name = '" + user.getUsername() + "' successfully registered.");
        }

        return resId;
    }

    @Override
    public AuthenticationToken login(UserName username, UserPassword password)
            throws AuthenticationException {

        checkNotNull(username, "Username cannot be null");
        checkNotNull(password, "Password cannot be null");

        if (log.isDebugEnabled()) {
            log.debug("Trying to authenticated user with username = '"
                    + username.getUsername() + "'...");
        }

        Optional<User> user = userRepository.getUserByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            AuthenticationToken token = new AuthenticationToken(username.getUsername());
            AuthenticatedUser authenticatedUser = new AuthenticatedUser(token, user.get().getId());
            userAuthenticationRepository.insert(authenticatedUser);
            if (log.isDebugEnabled()) {
                log.debug("User with username = '"
                        + username.getUsername() + "' successfully authenticated.");
            }
            return token;
        }

        if (log.isDebugEnabled()) {
            log.debug("Failed attempt to authenticate user with username = '"
                + username.getUsername() + "'.");
        }
        throw new AuthenticationException();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    @Override
    public Collection<User> getAllRegisteredUsers() {
        return userRepository.findAll();
    }

    @Override
    public void terminateAuthentication(AuthenticationToken token) {
        userAuthenticationRepository.deleteByToken(token);
    }

    @Override
    public Collection<AuthenticatedUser> getAllAuthenticatedUsers() {
        return userAuthenticationRepository.findAll();
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

    public static UserServiceImpl getInstance() {
        if (instance == null) {
            return instance = new UserServiceImpl();
        }
        return instance;
    }
}
