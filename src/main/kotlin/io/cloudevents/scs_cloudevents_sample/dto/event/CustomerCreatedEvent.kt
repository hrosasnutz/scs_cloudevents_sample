package io.cloudevents.scs_cloudevents_sample.dto.event

import java.time.LocalDate
import java.util.UUID

data class CustomerCreatedEvent(
    val uuid: UUID,
    val name: String?,
    val vip: Boolean,
    val birthdate: LocalDate?
)