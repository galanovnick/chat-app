package controller;

public enum HttpRequestMethod {
    GET("GET"),
    POST("POST"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String method;

    /*package*/ HttpRequestMethod(String method) {
        this.method = method;
    }

    public static HttpRequestMethod forName(String method) {
        for (HttpRequestMethod requestMethod: HttpRequestMethod.values()) {
            if (requestMethod.method.equalsIgnoreCase(method)) {
                return requestMethod;
            }
        }
        throw new IllegalStateException(String.format("Method \"%s\" not supported.", method));
    }
}
