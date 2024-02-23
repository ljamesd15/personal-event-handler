package com.personal.eventhandler.error;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.charset.StandardCharsets;

import static com.personal.eventhandler.fixtures.AmqpMessageFixture.MAX_RETRIES;
import static com.personal.eventhandler.fixtures.AmqpMessageFixture.X_DELAY;
import static com.personal.eventhandler.fixtures.AmqpMessageFixture.X_RETRY_COUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WeatherMessageErrorHandlerTest {

    @Test
    void builderTest() {
        RepublishMessageRecoverer retryMock = mock(RepublishMessageRecoverer.class);
        RepublishMessageRecoverer parkingLotMock = mock(RepublishMessageRecoverer.class);
        ErrorUtil errorUtil = new ErrorUtil();

        WeatherMessageErrorHandler underTest = WeatherMessageErrorHandler.builder()
                .retryMessageRecoverer(retryMock)
                .parkingLotMessageRecoverer(parkingLotMock)
                .errorUtil(errorUtil)
                .build();

        assertEquals(retryMock, underTest.getRetryMessageRecoverer());
        assertEquals(parkingLotMock, underTest.getParkingLotMessageRecoverer());
        assertEquals(errorUtil, underTest.getErrorUtil());
    }

    @Test
    void handleErrorTest_withNonRetryableException() {
        MessageProperties properties = new MessageProperties();
        Message message = new Message("Test".getBytes(StandardCharsets.UTF_8), properties);
        ListenerExecutionFailedException exception = new ListenerExecutionFailedException("Test",
                new HttpClientErrorException(HttpStatusCode.valueOf(400)));

        var mockSpringMessage = mock(org.springframework.messaging.Message.class);
        RepublishMessageRecoverer retryMock = mock(RepublishMessageRecoverer.class);
        RepublishMessageRecoverer parkingLotMock = mock(RepublishMessageRecoverer.class);

        WeatherMessageErrorHandler underTest = WeatherMessageErrorHandler.builder()
                .retryMessageRecoverer(retryMock)
                .parkingLotMessageRecoverer(parkingLotMock)
                .errorUtil(new ErrorUtil())
                .build();

        Object result = underTest.handleError(message, mockSpringMessage, exception);

        assertNull(result);
        verify(retryMock, never()).recover(any(Message.class), any(Throwable.class));
        verify(parkingLotMock, times(1)).recover(message, exception.getCause());
    }

    @Test
    void handleErrorTest_withMaxRetries() {
        MessageProperties properties = new MessageProperties();
        properties.setHeader(X_RETRY_COUNT, MAX_RETRIES);
        Message message = new Message("Test".getBytes(StandardCharsets.UTF_8), properties);
        ListenerExecutionFailedException exception = new ListenerExecutionFailedException("Test",
                new HttpServerErrorException(HttpStatusCode.valueOf(500)));

        var mockSpringMessage = mock(org.springframework.messaging.Message.class);
        RepublishMessageRecoverer retryMock = mock(RepublishMessageRecoverer.class);
        RepublishMessageRecoverer parkingLotMock = mock(RepublishMessageRecoverer.class);

        WeatherMessageErrorHandler underTest = WeatherMessageErrorHandler.builder()
                .retryMessageRecoverer(retryMock)
                .parkingLotMessageRecoverer(parkingLotMock)
                .errorUtil(new ErrorUtil())
                .build();

        Object result = underTest.handleError(message, mockSpringMessage, exception);

        assertNull(result);
        verify(retryMock, never()).recover(any(Message.class), any(Throwable.class));
        verify(parkingLotMock, times(1)).recover(message, exception.getCause());
    }


    @Test
    void handleErrorTest_withRetryException() {
        MessageProperties properties = new MessageProperties();
        Message message = new Message("Test".getBytes(StandardCharsets.UTF_8), properties);
        ListenerExecutionFailedException exception = new ListenerExecutionFailedException("Test",
                new HttpServerErrorException(HttpStatusCode.valueOf(500)));

        var mockSpringMessage = mock(org.springframework.messaging.Message.class);
        RepublishMessageRecoverer retryMock = mock(RepublishMessageRecoverer.class);
        RepublishMessageRecoverer parkingLotMock = mock(RepublishMessageRecoverer.class);

        WeatherMessageErrorHandler underTest = WeatherMessageErrorHandler.builder()
                .retryMessageRecoverer(retryMock)
                .parkingLotMessageRecoverer(parkingLotMock)
                .errorUtil(new ErrorUtil())
                .build();

        Object result = underTest.handleError(message, mockSpringMessage, exception);

        assertNull(result);
        verify(parkingLotMock, never()).recover(any(Message.class), any(Throwable.class));
        verify(retryMock, times(1)).recover(message, exception.getCause());
        assertEquals(1, Integer.parseInt(properties.getHeader(X_RETRY_COUNT).toString()));
        assertNotNull(properties.getHeader(X_DELAY));
    }


    @Test
    void handleErrorTest_withRetryExceptionAgain() {
        MessageProperties properties = new MessageProperties();
        properties.setHeader(X_RETRY_COUNT, 2);
        Message message = new Message("Test".getBytes(StandardCharsets.UTF_8), properties);
        ListenerExecutionFailedException exception = new ListenerExecutionFailedException("Test",
                new HttpServerErrorException(HttpStatusCode.valueOf(500)));

        var mockSpringMessage = mock(org.springframework.messaging.Message.class);
        RepublishMessageRecoverer retryMock = mock(RepublishMessageRecoverer.class);
        RepublishMessageRecoverer parkingLotMock = mock(RepublishMessageRecoverer.class);

        WeatherMessageErrorHandler underTest = WeatherMessageErrorHandler.builder()
                .retryMessageRecoverer(retryMock)
                .parkingLotMessageRecoverer(parkingLotMock)
                .errorUtil(new ErrorUtil())
                .build();

        Object result = underTest.handleError(message, mockSpringMessage, exception);

        assertNull(result);
        verify(parkingLotMock, never()).recover(any(Message.class), any(Throwable.class));
        verify(retryMock, times(1)).recover(message, exception.getCause());
        assertEquals(3, Integer.parseInt(properties.getHeader(X_RETRY_COUNT).toString()));
        assertNotNull(properties.getHeader(X_DELAY));
    }
}
