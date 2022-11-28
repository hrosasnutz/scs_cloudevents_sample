package io.cloudevents.scs_cloudevents_sample.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import io.cloudevents.CloudEvent
import io.cloudevents.core.builder.CloudEventBuilder
import io.cloudevents.core.data.PojoCloudEventData
import io.cloudevents.core.provider.EventFormatProvider
import io.cloudevents.jackson.JsonFormat
import io.cloudevents.scs_cloudevents_sample.ScsCloudeventsSampleApplicationTests
import io.cloudevents.scs_cloudevents_sample.dto.event.CustomerCreatedEvent
import io.cloudevents.scs_cloudevents_sample.factory.Factories
import io.cloudevents.scs_cloudevents_sample.mapper.CloudEventMessageMapper
import io.cloudevents.spring.messaging.CloudEventMessageConverter
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.function.cloudevent.CloudEventMessageUtils
import org.springframework.messaging.Message
import java.net.URI
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

class CloudEventsSerializationTests: ScsCloudeventsSampleApplicationTests() {

    private val logger = KotlinLogging.logger {  }

    @Autowired
    private lateinit var objectMapper: ObjectMapper
    
    @Autowired
    private lateinit var ceConverter: CloudEventMessageConverter
    
    @Autowired
    private lateinit var ceMapper: CloudEventMessageMapper

    private val customerCreatedEvent = CustomerCreatedEvent(
        UUID.randomUUID(),
        "BOB",
        false,
        LocalDate.now().minusYears(20)
    )
    @Test
    fun cloudEventSerialized() {
        val cloudEvent = CloudEventBuilder.v1()
            .withId(customerCreatedEvent.uuid.toString())
            .withSource(URI("/customers/${customerCreatedEvent.uuid}"))
            .withType(customerCreatedEvent::class.java.name)
            .withTime(OffsetDateTime.now())
            .withData(PojoCloudEventData.wrap(customerCreatedEvent){ objectMapper.writeValueAsBytes(it) })
            .build()
        logger.debug { "cloud event: $cloudEvent" }

        val serialized = EventFormatProvider
            .getInstance()
            .resolveFormat(JsonFormat.CONTENT_TYPE)
            ?.serialize(cloudEvent);
        Assertions.assertThat(serialized).isNotNull
        logger.debug { "serialized: $serialized" }

        val json = String(serialized!!)
        Assertions.assertThat(json).isNotEmpty
        logger.debug { "string: $json" }
    }
    
    @Test
    fun cloudEventMimeType() {
        val cloudEventsMimeType = CloudEventMessageUtils.APPLICATION_CLOUDEVENTS
        logger.debug { "type: ${cloudEventsMimeType.type}, subtype: ${cloudEventsMimeType.subtype}" }
    }
    
    @Test
    fun cloudEventMessageConverter() {
        val payload = ceMapper.to(Factories.getCreateCustomerCommand()) 
        val message = ceConverter.toMessage(payload.payload, payload.headers)
        logger.debug { "message: $message" }
        
        val cloudEvent = message?.let { ceConverter.fromMessage(it, CloudEvent::class.java) }
        Assertions.assertThat(cloudEvent).isNotNull
        logger.debug { "cloudEvent: $cloudEvent" }
    }
}