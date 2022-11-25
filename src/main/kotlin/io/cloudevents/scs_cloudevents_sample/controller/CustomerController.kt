package io.cloudevents.scs_cloudevents_sample.controller

import io.cloudevents.scs_cloudevents_sample.dto.command.CreateCustomerCommand
import io.cloudevents.scs_cloudevents_sample.service.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Optional
import java.util.UUID

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {
    
    @GetMapping
    fun getAll(): ResponseEntity<Any> {
        return ResponseEntity.ok(customerService.getAll())
    }
    
    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<Any> {
        return ResponseEntity.of(Optional.ofNullable(customerService.getById(uuid)))
    }
    
    @PostMapping
    fun createCustomer(@RequestBody createCustomerCommand: CreateCustomerCommand): ResponseEntity<Any> {
        customerService.create(createCustomerCommand)
        return ResponseEntity.accepted().build()
    }
}