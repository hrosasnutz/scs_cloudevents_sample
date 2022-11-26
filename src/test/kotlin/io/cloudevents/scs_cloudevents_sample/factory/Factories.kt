package io.cloudevents.scs_cloudevents_sample.factory

import io.cloudevents.scs_cloudevents_sample.dto.command.CreateCustomerCommand
import io.cloudevents.scs_cloudevents_sample.dto.event.CustomerCreatedEvent
import io.cloudevents.scs_cloudevents_sample.model.Customer
import io.github.serpro69.kfaker.faker
import java.time.LocalDate
import java.util.UUID

object Factories {
    
    private val faker = faker {  }
    
    fun getCreateCustomerCommand(): CreateCustomerCommand {
       return  CreateCustomerCommand(
           faker.name.name(),
           faker.random.nextBoolean(),
           LocalDate.now().minusYears(faker.random.nextLong(20))
       )
    }
    
    fun getCustomerCreatedEvent(): CustomerCreatedEvent {
        return  CustomerCreatedEvent(
            UUID.fromString(faker.random.nextUUID()),
            faker.name.name(),
            faker.random.nextBoolean(),
            LocalDate.now().minusYears(faker.random.nextLong(20))
        )
    }
    
    fun getCustomer(): Customer {
        return Customer(
            UUID.fromString(faker.random.nextUUID()),
            faker.name.name(),
            faker.random.nextBoolean(),
            LocalDate.now().minusYears(faker.random.nextLong(20))
        )
    }
}