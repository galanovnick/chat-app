package handler;

import java.util.Optional;

public interface HandlerRegistry {

    Optional<Handler> getHandler(UrlMethodPair key);

    void register(UrlMethodPair key, Handler handler);
}
