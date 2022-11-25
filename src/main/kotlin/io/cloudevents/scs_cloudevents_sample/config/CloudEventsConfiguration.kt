package io.cloudevents.scs_cloudevents_sample.config

import io.cloudevents.spring.messaging.CloudEventMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CloudEventsConfiguration {

    @Bean
    fun cloudEventMessageConverter(): CloudEventMessageConverter {
        return CloudEventMessageConverter();
    }
}