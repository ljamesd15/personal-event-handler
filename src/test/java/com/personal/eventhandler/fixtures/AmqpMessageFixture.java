package com.personal.eventhandler.fixtures;

public interface AmqpMessageFixture {
    String X_DELAY = "x-delay";
    String X_RETRY_COUNT = "x-retry-count";
    int MAX_RETRIES = 3;
}
