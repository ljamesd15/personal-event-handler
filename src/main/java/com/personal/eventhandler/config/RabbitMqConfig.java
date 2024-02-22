package com.personal.eventhandler.config;

import com.personal.eventhandler.error.WeatherMessageErrorHandler;
import com.rabbitmq.client.Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMqConfig {

    @Bean
    @Qualifier("WeatherRabbitMqPassword")
    public String weatherRabbitMqPassword(@Value("${rabbitmq.password}") final String password) {
        // TODO: Integrate with secret storage instead of env variables
        return password;
    }

    @Bean
    @Qualifier("WeatherRabbitMq")
    public Address weatherRabbitMqAddress(@Value("${rabbitmq.host}") final String host,
                                          @Value("${rabbitmq.port}") final int port) {
        return new Address(host, port);
    }

    @Bean
    public ConnectionFactory connectionFactory(@Qualifier("WeatherRabbitMq") final Address address,
                                               @Value("${rabbitmq.username}") final String username,
                                               @Qualifier("WeatherRabbitMqPassword") final String password,
                                               @Value("${rabbitmq.vhost}") final String vhost) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(address.toString());
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(vhost);
        return connectionFactory;
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

    @Bean("WeatherMessageErrorHandler")
    public RabbitListenerErrorHandler weatherErrorHandler() {
        return new WeatherMessageErrorHandler();
    }
}
