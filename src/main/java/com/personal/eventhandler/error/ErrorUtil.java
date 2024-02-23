package com.personal.eventhandler.error;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class ErrorUtil {

    // 1 min
    private static final int MAX_JITTER_IN_MILLIS = 60 * 1000;
    private static final int BACKOFF_MULTIPLIER = 2;
    // 5 min
    private static final int BACKOFF_INITIAL_IN_MILLIS = 5 * 60 * 1000;

    private final Random random;

    public ErrorUtil() {
        this.random = new Random();
    }

    public int getRetryBackoffInMillis(final int attempts) {
        final int jitter = random.nextInt(MAX_JITTER_IN_MILLIS);
        return BACKOFF_INITIAL_IN_MILLIS * BACKOFF_MULTIPLIER * (attempts + 1) + jitter;
    }
}
