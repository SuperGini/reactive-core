package com.gini.service;

import com.gini.dto.request.CustomerRequest;
import com.gini.dto.response.CustomerResponse;
import com.gini.exceptions.CustomerNotFoundException;
import com.gini.mapper.request.CustomerRequestMapper;
import com.gini.mapper.response.CustomerResponseMapper;
import com.gini.model.Customer;
import com.gini.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerRequestMapper customerRequestMapper;
    private final CustomerResponseMapper customerResponseMapper;


    public Mono<CustomerResponse> saveCustomer(CustomerRequest customerRequest) {

        Customer customer = customerRequestMapper.mapFrom(customerRequest);

        return customerRepository
                .save(customer)
                .map(customerResponseMapper::mapFrom)
                .log();
    }

    public Mono<CustomerResponse> findCustomerByUsername(String username) {
        // finds the customer. If the customer is not found it will switch and throw a Mono.error()
        // if the customer is found it will skip switchIfEmpty() and map the response
        return customerRepository.findCustomerByUsername(username)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found........")))
                .map(customerResponseMapper::mapFrom)
                .log();
    }
}
