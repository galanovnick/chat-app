package handler;

import java.util.Optional;

public interface HandlerRegistration {

    Optional<Handler> getHandler(String path);

    void register(String path, Handler handler);
}
