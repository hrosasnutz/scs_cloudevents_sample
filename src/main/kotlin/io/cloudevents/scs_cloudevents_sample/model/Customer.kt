package io.cloudevents.scs_cloudevents_sample.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "customer")
data class Customer (
    @Id
    var uuid: UUID = UUID.randomUUID(),
    var name: String? = null,
    var vip: Boolean = false,
    var birthdate: LocalDate? = null
) {
    constructor(name: String, vip: Boolean, birthdate: LocalDate) : this() {
        this.uuid = UUID.randomUUID()
        this.name = name
        this.vip = vip
        this.birthdate = birthdate
    }
}