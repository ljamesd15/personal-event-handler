package com.personal.eventhandler.handlers.impl;

import com.personal.eventhandler.handlers.EventHandler;
import com.personal.eventhandler.service.WeatherService;
import com.weather.model.external.request.SaveWeatherDataRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Handler to process Weather platform events.
 */
@Component
@Slf4j
public class RabbitMqWeatherEventHandler implements EventHandler<SaveWeatherDataRequest> {

    private final WeatherService weatherService;

    @Autowired
    public RabbitMqWeatherEventHandler(final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(
                                    name = "${rabbitmq.weather.queueName}",
                                    durable = "true",
                                    autoDelete = "false"
                            ),
                            exchange = @Exchange(
                                    name = "${rabbitmq.weather.exchangeName}",
                                    declare = "false"
                            ),
                            key = "${rabbitmq.weather.routingKey}"
                    ),
                    @QueueBinding(
                            value = @Queue(
                                    name = "${rabbitmq.weather.queueName}",
                                    durable = "true",
                                    autoDelete = "false"
                            ),
                            exchange = @Exchange(
                                    name = "${rabbitmq.weather.errorExchangeName}",
                                    delayed = "true"
                            ),
                            key = "${rabbitmq.weather.errorRoutingKey}"
                    ),
            },
            errorHandler = "WeatherMessageErrorHandler"
    )
    public void receiveMessage(@Payload final SaveWeatherDataRequest weatherData) {
        this.weatherService.saveWeatherData(weatherData);
    }
}
