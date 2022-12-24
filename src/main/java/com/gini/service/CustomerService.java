package com.gini.service;

import com.gini.dto.request.AddressRequest;
import com.gini.dto.request.CustomerRequest;
import com.gini.dto.response.CustomerResponse;
import com.gini.exceptions.CustomerAlreadyExistsException;
import com.gini.exceptions.CustomerNotFoundException;
import com.gini.mapper.request.AddressRequestMapper;
import com.gini.mapper.request.CustomerRequestMapper;
import com.gini.mapper.response.CustomerResponseMapper;
import com.gini.model.Customer;
import com.gini.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerRequestMapper customerRequestMapper;
    private final CustomerResponseMapper customerResponseMapper;
    private final AddressRequestMapper addressRequestMapper;


    public Mono<CustomerResponse> saveCustomer(CustomerRequest customerRequest) {
        var customer = customerRequestMapper.mapFrom(customerRequest);

        return customerRepository.findCustomerByUsername(customerRequest.getUsername())
                .doOnNext(this::throwErrorIfCustomerExists)
                .switchIfEmpty(customerRepository.save(customer))
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

    public Flux<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll()
                .map(customerResponseMapper::mapFrom)
                .delayElements(Duration.ofSeconds(2)) //delay elements to see if the flux works
                .log();
    }

    public Mono<CustomerResponse> updateCustomerAddress(AddressRequest addressRequest, String username) {

        return customerRepository.findCustomerByUsername(username)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found........")))
                .map(c -> setCustomerAddress(addressRequest, c))
                .flatMap(customerRepository::save)
                .map(customerResponseMapper::mapFrom)
                .log();
    }

    public Mono<Void> deleteCustomerByUsername(String username){
        return customerRepository.findCustomerByUsername(username)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer with username: " + username + " does not exists")))
                .flatMap(x -> customerRepository.deleteCustomerByUsername(x.getUsername()));
    }




    private Customer setCustomerAddress(AddressRequest addressRequest, Customer customer) {
        customer.setAddress(addressRequestMapper.mapFrom(addressRequest));
        return customer;
    }

    private void throwErrorIfCustomerExists(Customer customer) {
        if (customer != null) {
            log.error("Customer with username: {} --> already exists", customer.getUsername());
            throw new CustomerAlreadyExistsException("Customer already exists");
        }
    }
}
