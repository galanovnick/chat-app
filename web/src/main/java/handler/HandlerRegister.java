package handler;

import controller.Controller;

import java.util.Optional;

public interface HandlerRegister {

    Optional<Handler> getHandler(UrlMethodPair key);

    void register(UrlMethodPair key, Handler handler);

    void registerController(Class<? extends Controller> controllerClass);
}
