package com.personal.eventhandler.config;

import com.personal.eventhandler.error.ErrorUtil;
import com.personal.eventhandler.error.WeatherMessageErrorHandler;
import com.rabbitmq.client.Address;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherRabbitMq {

    @Bean
    @Qualifier("WeatherRabbitMqPassword")
    public String weatherRabbitMqPassword(@Value("${rabbitmq.weather.password}") final String password) {
        // TODO: Integrate with secret storage instead of env variables
        return password;
    }

    @Bean
    public ConnectionFactory connectionFactory(final Address address,
                                               @Value("${rabbitmq.weather.username}") final String username,
                                               @Qualifier("WeatherRabbitMqPassword") final String password,
                                               @Value("${rabbitmq.weather.vhost}") final String vhost) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(address.toString());
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(vhost);
        return connectionFactory;
    }

    @Bean
    @Qualifier("weather")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    @Qualifier("weatherRetry")
    public RepublishMessageRecoverer republishRetry(@Qualifier("weather") RabbitTemplate rabbitTemplate,
                                                    @Value("${rabbitmq.weather.errorExchangeName}")
                                                        final String errorExchange,
                                                    @Value("${rabbitmq.weather.errorRoutingKey}")
                                                        final String errorRoutingKey) {
        return new RepublishMessageRecoverer(rabbitTemplate, errorExchange, errorRoutingKey);
    }

    @Bean
    @Qualifier("weatherParkingLot")
    public RepublishMessageRecoverer republishParkingLot(@Qualifier("weather") RabbitTemplate rabbitTemplate,
                                                         @Value("${rabbitmq.weather.parkingLotExchangeName}")
                                                            final String parkingLot,
                                                         @Value("${rabbitmq.weather.parkingLotRoutingKey}")
                                                             final String errorRoutingKey) {
        return new RepublishMessageRecoverer(rabbitTemplate, parkingLot, errorRoutingKey);
    }

    @Bean
    @Qualifier("WeatherParkingLot")
    public Queue parkingLotQueue(@Value("${rabbitmq.weather.parkingLotQueueName}")
                                     final String parkingLot) {
        return new Queue(parkingLot, true, false, false);
    }

    @Bean
    @Qualifier("WeatherParkingLot")
    public DirectExchange parkingLotExchange(@Value("${rabbitmq.weather.parkingLotExchangeName}")
                                                 final String parkingLot) {
        return new DirectExchange(parkingLot, true, false);
    }

    @Bean
    public Binding parkingLotBinding(@Qualifier("WeatherParkingLot") Queue queue,
                                     @Qualifier("WeatherParkingLot") DirectExchange exchange,
                                     @Value("${rabbitmq.weather.parkingLotRoutingKey}")
                                         final String errorRoutingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(errorRoutingKey);
    }

    @Bean("WeatherMessageErrorHandler")
    public RabbitListenerErrorHandler weatherErrorHandler(@Qualifier("weatherRetry") RepublishMessageRecoverer retry,
                                                          @Qualifier("weatherParkingLot")
                                                            RepublishMessageRecoverer parkingLot) {
        return WeatherMessageErrorHandler.builder()
                .parkingLotMessageRecoverer(parkingLot)
                .retryMessageRecoverer(retry)
                .errorUtil(new ErrorUtil())
                .build();
    }
}
