package io.cloudevents.scs_cloudevents_sample.repository

import io.cloudevents.scs_cloudevents_sample.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerRepository: JpaRepository<Customer, UUID> {
}