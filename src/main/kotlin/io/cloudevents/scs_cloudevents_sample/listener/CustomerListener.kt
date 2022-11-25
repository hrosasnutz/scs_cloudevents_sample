package io.cloudevents.scs_cloudevents_sample.listener

import com.fasterxml.jackson.databind.ObjectMapper
import io.cloudevents.CloudEvent
import io.cloudevents.scs_cloudevents_sample.dto.command.CreateCustomerCommand
import io.cloudevents.scs_cloudevents_sample.dto.event.CustomerCreatedEvent
import io.cloudevents.scs_cloudevents_sample.mapper.CloudEventMessageMapper
import io.cloudevents.scs_cloudevents_sample.service.CustomerService
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class CustomerListener(
    private val customerService: CustomerService,
    private val cloudEventMessageMapper: CloudEventMessageMapper
) {
    
    private val logger = KotlinLogging.logger {  }
    
    
    @Bean
    fun saveCustomer(): (Flux<Message<CloudEvent>>) -> Flux<Message<CloudEvent>> {
        return { ce -> ce
            .doOnNext { logger.debug { "To save Customer: $it" } }
            .mapNotNull { msg -> cloudEventMessageMapper.toPojo(msg.payload, CreateCustomerCommand::class.java) }
            .flatMap { cmd -> Mono.fromCallable { 
                    customerService.save(cmd!!)
                }
            }.map { evt -> cloudEventMessageMapper.to(evt) }
        }
    }

    @Bean
    fun logSavedCustomer(): (Flux<Message<CloudEvent>>) -> Mono<Void> {
        return { ce -> ce
            .doOnNext { logger.debug { "customer created message: $it" } }
            .map { cloudEventMessageMapper.toPojo(it.payload, CustomerCreatedEvent::class.java) }
            .doOnNext { logger.info { "New customer created: $it" } }
            .then()
        }
    }
    
    // @Bean
    // fun saveCustomer(): (CloudEvent) -> Message<CloudEvent> {
    //     return {
    //         logger.debug { "To save Customer: $it" }
    //         val cmd = ToCloudEventMessage.toPojo(it, objectMapper, CreateCustomerCommand::class.java)
    //         val evt = customerService.save(cmd!!)
    //         ToCloudEventMessage.to(evt, objectMapper)
    //     }
    // }
}