package io.cloudevents.scs_cloudevents_sample.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.cloudevents.scs_cloudevents_sample.dto.command.CreateCustomerCommand
import io.cloudevents.scs_cloudevents_sample.dto.event.CustomerCreatedEvent
import io.cloudevents.scs_cloudevents_sample.mapper.CloudEventMessageMapper
import io.cloudevents.scs_cloudevents_sample.model.Customer
import io.cloudevents.scs_cloudevents_sample.repository.CustomerRepository
import mu.KotlinLogging
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val streamBridge: StreamBridge,
    private val cloudEventMessageMapper: CloudEventMessageMapper
) {
    
    private val logger = KotlinLogging.logger {  }
    
    fun getAll(): List<Customer> {
        return customerRepository.findAll()
    }
    
    fun getById(uuid: UUID): Customer? {
        return customerRepository.findByIdOrNull(uuid)
    }
    
    fun create(cmd: CreateCustomerCommand) {
        logger.debug { "Send command: $cmd" }
        streamBridge.send("httpCustomer-out-0", cloudEventMessageMapper.to(cmd))
    }

    @Transactional
    fun save(cmd: CreateCustomerCommand): CustomerCreatedEvent {
        var customer = Customer(cmd.name, cmd.vip, cmd.birthdate)
        customer = customerRepository.save(customer)
        logger.info { "New customer was created with ${customer.uuid}" }
        return CustomerCreatedEvent(
            customer.uuid, customer.name, customer.vip, customer.birthdate
        )
    }
}