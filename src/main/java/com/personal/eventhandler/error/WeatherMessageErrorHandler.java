package com.personal.eventhandler.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.web.client.HttpClientErrorException;

@Log4j2
public class WeatherMessageErrorHandler implements RabbitListenerErrorHandler {

    @Override
    public Object handleError(final Message message,
                              final org.springframework.messaging.Message<?> springMessage,
                              final ListenerExecutionFailedException e) throws Exception {
        if (e.getCause() instanceof HttpClientErrorException) {
            log.info("Is client error exception");
            // TODO: Setup retries and DLQ
            throw new AmqpRejectAndDontRequeueException(e);
        }
        // TODO: Send to a parking lot queue to not be retried
        throw new AmqpRejectAndDontRequeueException(e);
    }
}
