package com.personal.eventhandler.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AwsConfig {

    @Bean
    @Qualifier("WeatherService")
    public AwsCredentialsProvider getWeatherServiceAwsCredentials(@Value("${spring.cloud.aws.credentials.access-key}") final String accessKey,
                                                                  @Value("${spring.cloud.aws.credentials.secret-key}") final String secretKey) {
        return StaticCredentialsProvider.create(AwsBasicCredentials.builder()
                .accessKeyId(accessKey)
                .secretAccessKey(secretKey)
                .build()
            );
    }

    @Bean
    @Qualifier("Weather")
    public SqsClient getWeatherSqsClient(@Qualifier("WeatherService") final AwsCredentialsProvider creds,
                                         @Value("${spring.cloud.aws.region.static}") final String region) {
        return SqsClient.builder()
                .credentialsProvider(creds)
                .region(Region.of(region))
                .build();
    }
}
