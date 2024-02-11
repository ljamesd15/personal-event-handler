package com.personal.eventhandler.handlers;

/**
 * Methods that all event handlers must implement.
 */
public interface EventHandler<T> {

    void receiveMessage(T message);
}
