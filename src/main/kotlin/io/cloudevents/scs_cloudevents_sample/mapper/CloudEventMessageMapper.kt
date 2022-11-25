package io.cloudevents.scs_cloudevents_sample.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import io.cloudevents.CloudEvent
import io.cloudevents.CloudEventData
import io.cloudevents.core.CloudEventUtils
import io.cloudevents.core.builder.CloudEventBuilder
import io.cloudevents.core.data.PojoCloudEventData
import io.cloudevents.jackson.PojoCloudEventDataMapper
import io.cloudevents.scs_cloudevents_sample.dto.command.CreateCustomerCommand
import io.cloudevents.scs_cloudevents_sample.dto.event.CustomerCreatedEvent
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import org.springframework.util.MimeTypeUtils
import java.net.URI
import java.time.OffsetDateTime
import java.util.UUID

@Component
class CloudEventMessageMapper(
    private val objectMapper: ObjectMapper
) {

    fun to(command: CreateCustomerCommand): Message<CloudEvent> {
        val event = CloudEventBuilder.v1()
            .withId(UUID.randomUUID().toString())
            .withSource(URI.create("/api/customers"))
            .withType(CreateCustomerCommand::class.java.name)
            .withSubject("customer")
            .withTime(OffsetDateTime.now())
            .withData(MimeTypeUtils.APPLICATION_JSON_VALUE, toCloudEventData(command))
            .build()
        return MessageBuilder.withPayload(event)
            .setHeader(KafkaHeaders.MESSAGE_KEY, event.id.toByteArray())
            .build()
    }

    fun to(event: CustomerCreatedEvent): Message<CloudEvent> {
        val event = CloudEventBuilder.v1()
            .withId(event.uuid.toString())
            .withSource(URI.create("/api/customers/${event.uuid}"))
            .withType(CustomerCreatedEvent::class.java.name)
            .withSubject("customer")
            .withTime(OffsetDateTime.now())
            .withData(MimeTypeUtils.APPLICATION_JSON_VALUE, toCloudEventData(event))
            .build()
        return MessageBuilder.withPayload(event)
            .setHeader(KafkaHeaders.MESSAGE_KEY, event.id.toByteArray())
            .build()
    }

    fun toCloudEventData(data: Any): CloudEventData {
        return PojoCloudEventData.wrap(data) {
            objectMapper.writeValueAsBytes(it)
        }
    }

    fun <T: Any> toPojo(cloudEvent: CloudEvent, clazz: Class<T>): T? {
        val data = CloudEventUtils.mapData(
            cloudEvent,
            PojoCloudEventDataMapper.from(objectMapper, clazz)
        )
        return data?.value
    }
}