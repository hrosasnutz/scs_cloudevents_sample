package io.cloudevents.scs_cloudevents_sample.service

import io.cloudevents.scs_cloudevents_sample.ScsCloudeventsSampleApplicationTests
import io.cloudevents.scs_cloudevents_sample.factory.Factories
import io.cloudevents.scs_cloudevents_sample.mapper.CloudEventMessageMapper
import io.cloudevents.scs_cloudevents_sample.model.Customer
import io.cloudevents.scs_cloudevents_sample.repository.CustomerRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.data.repository.findByIdOrNull
import java.util.Optional
import java.util.UUID

internal class CustomerServiceTest : ScsCloudeventsSampleApplicationTests() {
    
    private lateinit var customerService: CustomerService
    @Mock
    private lateinit var customerRepository: CustomerRepository
    @Mock
    private lateinit var streamBridge: StreamBridge
    @Autowired
    private lateinit var cloudEventMessageMapper: CloudEventMessageMapper

    @BeforeEach
    fun setUp() {
        customerService = CustomerService(customerRepository, streamBridge, cloudEventMessageMapper)
    }

    @Test
    fun getAll() {
        whenever(customerRepository.findAll())
            .thenReturn(listOf(Factories.getCustomer(), Factories.getCustomer(), Factories.getCustomer()))
        val customers = customerService.getAll()
        Assertions.assertThat(customers).isNotNull
        Assertions.assertThat(customers).isNotEmpty
    }

    @Test
    fun getById() {
        whenever(customerRepository.findById(any()))
            .thenReturn(Optional.of(Factories.getCustomer()))
        val customer = customerService.getById(UUID.randomUUID())
        Assertions.assertThat(customer).isNotNull
    }

    @Test
    fun create() {
        whenever(streamBridge.send(any<String>(), any()))
            .thenReturn(true)
        Assertions.assertThatCode {
            customerService.create(Factories.getCreateCustomerCommand())
        }.doesNotThrowAnyException()
    }

    @Test
    fun save() {
        whenever(customerRepository.save(any<Customer>()))
            .thenReturn(Factories.getCustomer())
        val event = customerService.save(Factories.getCreateCustomerCommand())
        Assertions.assertThat(event).isNotNull
    }
}