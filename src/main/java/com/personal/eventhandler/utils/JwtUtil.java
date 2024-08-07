package com.personal.eventhandler.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Log4j2
public class JwtUtil {

    private static final String BEARER_TOKEN_FORMAT = "Bearer %s";
    private static final String JWT_HEADER_TYPE_KEY = "typ";
    private static final String JWT_HEADER_TYPE_VALUE = "JWT";
    private static final int RELATIVE_EXPIRATION_IN_HOURS = 1;
    private static final String WEATHER_SUBJECT = "weather-event-handler";

    private final Clock clock;
    private final String weatherSecret;
    private final String weatherIssuer;
    private String jwt;
    private Date expiration;

    public JwtUtil(final Clock clock,
                   @Value("${weather.jwt.key-name}") final String weatherIssuer,
                   @Value("${weather.jwt.secret}") final String weatherSecret) {
        this.clock = clock;
        this.weatherIssuer = weatherIssuer;
        this.weatherSecret = weatherSecret;
    }

    public String getWeatherToken() {
        final Instant currentInstant = Instant.now(this.clock);
        final Date currentTime = Date.from(Instant.now(this.clock));
        if (this.expiration == null || currentTime.after(this.expiration)) {
            this.expiration = currentTime;
            this.jwt = Jwts.builder()
                    .header()
                    .add(JWT_HEADER_TYPE_KEY, JWT_HEADER_TYPE_VALUE)
                    .and()
                    .issuer(this.weatherIssuer)
                    .issuedAt(currentTime)
                    .notBefore(currentTime)
                    .expiration(Date.from(currentInstant.plus(RELATIVE_EXPIRATION_IN_HOURS, ChronoUnit.HOURS)))
                    .subject(WEATHER_SUBJECT)
                    .signWith(Keys.hmacShaKeyFor(
                                    Base64.getDecoder().decode(this.weatherSecret)
                            ),
                            SignatureAlgorithm.HS512)
                    .compact();
        }
        return String.format(BEARER_TOKEN_FORMAT, jwt);
    }
}
