package handler;

import controller.Controller;

import java.util.List;

public class ControllerInitializer {

    private final HandlerRegistry handlerRegistry;
    private final List<Controller> controllers;

    public ControllerInitializer(HandlerRegistry handlerRegistry, List<Controller> controllers) {
        this.handlerRegistry = handlerRegistry;
        this.controllers = controllers;
    }

    public HandlerRegistry getHandlerRegistry() {
        return handlerRegistry;
    }

    public List<Controller> getContollers() {
        return controllers;
    }
}
