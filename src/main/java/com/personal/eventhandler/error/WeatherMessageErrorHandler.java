package com.personal.eventhandler.error;

import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.web.client.HttpServerErrorException;

@Builder
@Data
@Log4j2
public class WeatherMessageErrorHandler implements RabbitListenerErrorHandler {

    private static final String X_DELAY = "x-delay";
    private static final String X_RETRY_COUNT = "x-retry-count";
    private static final int MAX_RETRIES = 3;

    private final RepublishMessageRecoverer retryMessageRecoverer;
    private final RepublishMessageRecoverer parkingLotMessageRecoverer;
    private final ErrorUtil errorUtil;

    @Override
    public Object handleError(final Message message,
                              final org.springframework.messaging.Message<?> springMessage,
                              final ListenerExecutionFailedException e) {
        final int retries = Optional.ofNullable(
                        message.getMessageProperties().getHeader(X_RETRY_COUNT)
                )
                .map(Object::toString)
                .map(Integer::parseInt)
                .orElse(0);
        if (retries >= MAX_RETRIES) {
            log.error("Reached maximum retries on message");
            this.parkingLotMessageRecoverer.recover(message, e.getCause());
        } else if (e.getCause() instanceof HttpServerErrorException) {
            message.getMessageProperties().setHeader(X_RETRY_COUNT, retries + 1);
            message.getMessageProperties().setHeader(X_DELAY, this.errorUtil.getRetryBackoffInMillis(retries));
            log.info(message);
            this.retryMessageRecoverer.recover(message, e.getCause());
        } else {
            log.error("Unrecoverable exception: {}", e.getCause().getMessage());
            this.parkingLotMessageRecoverer.recover(message, e.getCause());
        }
        return null;
    }
}
