package com.personal.eventhandler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {

    @Bean
    @Qualifier("Weather")
    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter(@Qualifier("Weather") final ObjectMapper mapper) {
        final MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        return converter;
    }

    @Bean
    @Qualifier("WeatherService")
    public RestClient restClient(final MappingJackson2HttpMessageConverter messageConverter,
                                 @Value("${weather.base-url}") final String baseUrl) {
        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .messageConverters(converters -> converters.add(messageConverter))
                .baseUrl(baseUrl)
                // TODO: Integrate with auth when weather platform has it
                //.defaultHeader("My-Header", "Foo")
                //.requestInterceptor(new LoggingInterceptor())
                .build();
    }
}
