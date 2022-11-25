package io.cloudevents.scs_cloudevents_sample.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import io.cloudevents.core.builder.CloudEventBuilder
import io.cloudevents.core.data.PojoCloudEventData
import io.cloudevents.core.provider.EventFormatProvider
import io.cloudevents.jackson.JsonFormat
import io.cloudevents.scs_cloudevents_sample.ScsCloudeventsSampleApplicationTests
import io.cloudevents.scs_cloudevents_sample.dto.event.CustomerCreatedEvent
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.net.URI
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

class CloudEventsSerializationTests: ScsCloudeventsSampleApplicationTests() {

    private val logger = KotlinLogging.logger {  }

    @Autowired
    private lateinit var objectMapper: ObjectMapper

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
}