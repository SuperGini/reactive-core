package com.gini.controller;

import com.gini.dto.request.AddressRequest;
import com.gini.dto.request.CustomerRequest;
import com.gini.dto.response.CustomerResponse;
import com.gini.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(value = "/customer", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerResponse> saveCustomer(@RequestBody CustomerRequest customerRequest) {

        return customerService.saveCustomer(customerRequest);
    }

    @GetMapping(value = "/customer/{username}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<CustomerResponse> getCustomer(@PathVariable String username) {

        return customerService.findCustomerByUsername(username);
    }

    @GetMapping("/customers")
    public Flux<CustomerResponse> getAllCustomers() {

        return customerService.findAllCustomers();
    }

    @PutMapping("/customer/{username}")
    public Mono<CustomerResponse> updateCustomerAddress(@RequestBody AddressRequest addressRequest,
                                                        @PathVariable String username) {

        return customerService.updateAddress(addressRequest, username);

    }


}
