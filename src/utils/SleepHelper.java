package utils;

import org.dreambot.api.methods.MethodContext;

import java.util.concurrent.ThreadLocalRandom;

public class SleepHelper extends MethodContext {
    public static void randomSleep(int min, int max) {
        sleep(ThreadLocalRandom.current().nextInt(min, max));
    }
}
