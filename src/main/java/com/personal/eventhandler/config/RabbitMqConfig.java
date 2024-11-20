package com.personal.eventhandler.config;

import com.rabbitmq.client.Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Address weatherRabbitMqAddress(@Value("${rabbitmq.host}") final String host,
                                          @Value("${rabbitmq.port}") final int port) {
        return new Address(host, port);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> listenerFactory(
            ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        final var factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(converter);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}
