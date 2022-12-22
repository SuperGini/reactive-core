package com.gini.controller;

import com.gini.model.Customer;
import com.gini.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;

    @PostMapping(value = "/customer", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Customer> saveCustomer(@RequestBody Customer customer) {

        Customer x = customer;


        return customerRepository.save(customer);

    }


}
