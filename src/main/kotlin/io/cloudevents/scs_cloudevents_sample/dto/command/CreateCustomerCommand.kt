package io.cloudevents.scs_cloudevents_sample.dto.command

import java.time.LocalDate

data class CreateCustomerCommand(
    val name: String,
    val vip: Boolean,
    val birthdate: LocalDate
)