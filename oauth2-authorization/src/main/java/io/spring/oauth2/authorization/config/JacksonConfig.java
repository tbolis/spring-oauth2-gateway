package io.spring.oauth2.authorization.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.spring.oauth2.domain.util.JSR310DateTimeSerializer;
import io.spring.oauth2.domain.util.JSR310LocalDateDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.inject.Singleton;
import java.time.*;

@Configuration
public class JacksonConfig {

    @Bean
    @Singleton
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        JavaTimeModule module = new JavaTimeModule();
        //@formatter:off
        module
                .addSerializer(Instant.class, JSR310DateTimeSerializer.INSTANCE)
                .addSerializer(ZonedDateTime.class, JSR310DateTimeSerializer.INSTANCE)
                .addSerializer(LocalDateTime.class, JSR310DateTimeSerializer.INSTANCE)
                .addSerializer(OffsetDateTime.class, JSR310DateTimeSerializer.INSTANCE)
                .addDeserializer(LocalDate.class, JSR310LocalDateDeserializer.INSTANCE);
        //@formatter:on
        return new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findModulesViaServiceLoader(true)
                .modulesToInstall(module);
    }
}
