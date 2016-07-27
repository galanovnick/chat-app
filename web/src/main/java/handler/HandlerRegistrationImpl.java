package handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerRegistrationImpl implements HandlerRegistration {

    private static HandlerRegistration instance;

    private final Map<String, Handler> handlers = new HashMap<>();

    @Override
    public Optional<Handler> getHandler(String path) {
        return Optional.ofNullable(handlers.get(path));
    }

    @Override
    public void register(String path, Handler handler) {
        handlers.put(path, handler);
    }

    private HandlerRegistrationImpl(){}

    public static HandlerRegistration getInstance() {
        if (instance == null) {
            return instance = new HandlerRegistrationImpl();
        }
        return instance;
    }
}
