package tr.com.muskar.MediaServer.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import tr.com.muskar.MediaServer.model.FailedFutureRecord;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

@Configuration
@RequiredArgsConstructor
public class ApplicationStartupConfig {

    @Bean
    public Map<String, Future<?>> streamTasks() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, FailedFutureRecord> failedStreamTask() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(module)
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module());
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return mapper;
    }
}
