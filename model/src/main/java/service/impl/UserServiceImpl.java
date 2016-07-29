package service.impl;

import entity.AuthenticationToken;
import entity.User;
import entity.tiny.user.UserId;
import entity.tiny.user.UserName;
import entity.tiny.user.UserPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.AuthenticationTokenRepository;
import repository.UserRepository;
import service.AuthenticationException;
import service.InvalidUserDataException;
import service.UserService;
import service.impl.dto.AuthenticationTokenDto;
import service.impl.dto.RegistrationDto;
import service.impl.dto.UserDto;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static UserServiceImpl instance;

    private final UserRepository userRepository = UserRepository.getInstance();
    private final AuthenticationTokenRepository tokenRepository
            = AuthenticationTokenRepository.getInstance();

    @Override
    public UserId registerUser(RegistrationDto userData) throws InvalidUserDataException {
        checkNotNull(userData, "User dto cannot be null.");

        if (log.isDebugEnabled()) {
            log.debug(String.format("Trying to register user with name ='%s'...",
                    userData.getUsername()));
        }

        if (userData.getUsername().equals("")
                || userData.getPassword().equals("")
                || userData.getPasswordConfirm().equals("")) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Failed attempt to register user with name " +
                        "= '%s'. Reason: empty fields.", userData.getUsername()));
            }
            throw new InvalidUserDataException("Fields cannot be empty.");
        }
        if (!userData.getPassword().equals(userData.getPasswordConfirm())) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Failed attempt to register user with name = '%s'. " +
                        "Reason: different passwords.", userData.getUsername()));
            }
            throw new InvalidUserDataException("Passwords do not match.");
        }
        if (userRepository.getUserByUsername(new UserName(userData.getUsername())).isPresent()) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Failed attempt to register user with name = '%s'. " +
                        "Reason: user already exists.", userData.getUsername()));
            }
            throw new InvalidUserDataException("User with such name already exists.");
        }
        final UserId resId = userRepository.add(
                new User(new UserName(userData.getUsername()),
                        new UserPassword(userData.getPassword())));

        if (log.isDebugEnabled()) {
            log.debug(String.format("User with name = '%s' successfully registered.",
                    userData.getUsername()));
        }

        return resId;
    }

    @Override
    public AuthenticationTokenDto login(UserName username, UserPassword password)
            throws AuthenticationException {

        checkNotNull(username, "Username cannot be null");
        checkNotNull(password, "Password cannot be null");

        if (username.value().equals("") || password.value().equals("")) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Failed attempt to authenticate user with username = '%s'." +
                                "Reason: empty fields.",
                        username.value()));
            }
            throw new AuthenticationException("Fields cannot be empty.");
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format("Trying to authenticated user with username = '%s'...",
                    username.value()));
        }

        Optional<User> user = userRepository.getUserByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password.value())) {
            AuthenticationToken token = new AuthenticationToken(
                    UUID.randomUUID().toString(),
                    user.get().getId().value());
            tokenRepository.add(token);
            if (log.isDebugEnabled()) {
                log.debug(String.format("User with username = '%s' successfully authenticated.",
                        username.value()));
            }
            return new AuthenticationTokenDto(token.getToken());
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format("Failed attempt to authenticate user with username = '%s'." +
                            "Reason: invalid username or password.",
                    username.value()));
        }
        throw new AuthenticationException("Invalid username or password.");
    }

    @Override
    public void deleteUser(UserId id) {
        userRepository.delete(id);
    }

    @Override
    public Collection<UserDto> getAllRegisteredUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(new UserName(user.getUsername())))
                .collect(Collectors.toList());
    }

    @Override
    public void logout(AuthenticationTokenDto user) {
        Optional<AuthenticationToken> token = tokenRepository.findByTokenString(user.getToken());
        if (token.isPresent()) {
            tokenRepository.delete(token.get().getId());
        } else {
            throw new IllegalStateException(
                    String.format("Attempt to terminate user session with invalid token: %s",
                            user.getToken()));
        }
    }

    @Override
    public Collection<AuthenticationTokenDto> getAllTokens() {
        return tokenRepository.findAll().stream()
                .map(token -> new AuthenticationTokenDto(token.getToken()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isUserRegistered(UserId id) {
        checkArgument(id.value() > -1, "Id cannot be negative.");

        return userRepository.findOne(id).isPresent();
    }

    @Override
    public Optional<AuthenticationTokenDto> checkAuthentication(AuthenticationTokenDto tokenDto) {

        checkNotNull(tokenDto, "Token cannot be null");

        Optional<AuthenticationToken> tokenEntity
                = tokenRepository.findByTokenString(tokenDto.getToken());

        if (tokenEntity.isPresent()) {
            return Optional.of(new AuthenticationTokenDto(
                    tokenEntity.get().getToken(),
                    tokenEntity.get().getUserId()
            ));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public UserDto getUser(UserId userId) {
        Optional<User> user = userRepository.findOne(userId);
        if (user.isPresent()) {
            return new UserDto(new UserName(user.get().getUsername()));
        }
        throw new IllegalStateException("Attempt to get user by invalid id.");
    }

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        if (instance == null) {
            return instance = new UserServiceImpl();
        }
        return instance;
    }
}
