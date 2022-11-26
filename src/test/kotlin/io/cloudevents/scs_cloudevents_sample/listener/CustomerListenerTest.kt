package io.cloudevents.scs_cloudevents_sample.listener

import io.cloudevents.scs_cloudevents_sample.ScsCloudeventsSampleApplicationTests
import io.cloudevents.scs_cloudevents_sample.factory.Factories
import io.cloudevents.scs_cloudevents_sample.mapper.CloudEventMessageMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

internal class CustomerListenerTest : ScsCloudeventsSampleApplicationTests() {
    
    @Autowired
    private lateinit var customerListener: CustomerListener
    @Autowired
    private lateinit var cloudEventMessageMapper: CloudEventMessageMapper
    
    @BeforeEach
    fun setUp() {
    }

    @Test
    fun saveCustomer() {
        val input = Flux.just(cloudEventMessageMapper.to(Factories.getCreateCustomerCommand()))
        val output = customerListener.saveCustomer()(input)
        StepVerifier.withVirtualTime { output }
            .expectNextCount(1)
            .verifyComplete()
    }

    @Test
    fun logSavedCustomer() {
        val input = Flux.just(cloudEventMessageMapper.to(Factories.getCustomerCreatedEvent()))
        val output = customerListener.logSavedCustomer()(input)
        StepVerifier.withVirtualTime { output }
            .verifyComplete()
    }
}