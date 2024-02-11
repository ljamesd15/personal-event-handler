package com.personal.eventhandler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import static org.mockito.Mockito.mock;

@Configuration
public class MockRestConfig {

    @Bean
    public RestClient mockRestClient() {
        return mock(RestClient.class);
    }
}
