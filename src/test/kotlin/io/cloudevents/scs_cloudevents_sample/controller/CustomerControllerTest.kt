package io.cloudevents.scs_cloudevents_sample.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import io.cloudevents.scs_cloudevents_sample.ScsCloudeventsSampleApplicationTests
import io.cloudevents.scs_cloudevents_sample.factory.Factories
import io.cloudevents.scs_cloudevents_sample.service.CustomerService

import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.UUID

@WebMvcTest(controllers = [CustomerController::class])
internal class CustomerControllerTest {
    
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @MockBean
    private lateinit var customerService: CustomerService
    
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        whenever(customerService.getAll())
            .thenReturn(listOf(Factories.getCustomer(), Factories.getCustomer()))
        whenever(customerService.getById(any()))
            .thenReturn(Factories.getCustomer())
        doNothing().whenever(customerService)
            .create(any())
    }

    @Test
    fun getAll() {
        mockMvc
            .get("/api/customers") {
                this.accept = MediaType.ALL
            }
            .andExpect {
                status { is2xxSuccessful() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
            }
    }

    @Test
    fun getById() {
        mockMvc
            .get("/api/customers/{uuid}", UUID.randomUUID()) {
                this.accept = MediaType.ALL
            }
            .andExpect {
                status { is2xxSuccessful() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
            }
    }

    @Test
    fun createCustomer() {
        mockMvc
            .post("/api/customers") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(Factories.getCreateCustomerCommand())
            }
            .andExpect { 
                status { isAccepted() }
            }
    }
}