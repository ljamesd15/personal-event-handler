package com.personal.eventhandler;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestClient;


@EnableRabbit
@SpringBootApplication
public class PersonalEventHandlerApplication {

    @Autowired @Qualifier("WeatherService")
    RestClient restClient;

    public static void main(String[] args) {
        SpringApplication.run(PersonalEventHandlerApplication.class, args);
    }
}
