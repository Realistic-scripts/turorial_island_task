package utils;

import org.dreambot.api.methods.MethodContext;

public class LogHelper extends MethodContext {
    public static void logMethod(Object logObject) {
        log(logObject);
    }
}
