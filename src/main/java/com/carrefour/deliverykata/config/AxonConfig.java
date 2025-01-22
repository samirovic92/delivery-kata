package com.carrefour.deliverykata.config;

import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class AxonConfig {

    @Primary
    @Bean
    public Serializer serializer() {
        return JacksonSerializer.defaultSerializer();
    }

    @Autowired
    public void configureErrorHandler(EventProcessingConfigurer configurer) {
        configurer.registerListenerInvocationErrorHandler(
                "order-group",
                configuration -> PropagatingErrorHandler.instance()
        );
    }
}
