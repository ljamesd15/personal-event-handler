package com.personal.eventhandler.config;

import com.rabbitmq.client.Address;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    @Qualifier("WeatherRabbitMqUsername")
    public String weatherRabbitMqUsername(@Value("${rabbitmq.username}") final String username) {
        return username;
    }

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
                                               @Qualifier("WeatherRabbitMqUsername") final String username,
                                               @Qualifier("WeatherRabbitMqPassword") final String password) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(address.toString());
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
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
}
