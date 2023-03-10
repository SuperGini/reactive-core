package com.gini.service;

import com.gini.dto.request.AddressRequest;
import com.gini.dto.request.BasketItemRequest;
import com.gini.dto.request.CustomerRequest;
import com.gini.dto.response.CustomerResponse;
import com.gini.exceptions.CustomerAlreadyExistsException;
import com.gini.exceptions.CustomerNotFoundException;
import com.gini.mapper.request.AddressRequestMapper;
import com.gini.mapper.request.BasketItemRequestMapper;
import com.gini.mapper.request.CustomerRequestMapper;
import com.gini.mapper.response.CustomerResponseMapper;
import com.gini.model.BasketItem;
import com.gini.model.Customer;
import com.gini.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

//https://stackoverflow.com/questions/53595420/correct-way-of-throwing-exceptions-with-reactor
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private static final String CUSTOMER_NOT_FOUND = "Customer not found........";

    private final CustomerRepository customerRepository;
    private final CustomerRequestMapper customerRequestMapper;
    private final CustomerResponseMapper customerResponseMapper;
    private final AddressRequestMapper addressRequestMapper;
    private final BasketItemRequestMapper basketItemRequestMapper;


    public Flux<CustomerResponse> saveCustomer(CustomerRequest customerRequest) {
        var customer = customerRequestMapper.mapFrom(customerRequest);

        return customerRepository.findCustomerByUsername(customerRequest.getUsername())
                .doOnNext(this::throwErrorIfCustomerExists)
                .switchIfEmpty(customerRepository.save(customer))
                .map(customerResponseMapper::mapFrom)
                .log();

    }

    public Flux<CustomerResponse> findCustomerByUsername(String username) {
        // finds the customer. If the customer is not found it will switch and throw a Mono.error()
        // if the customer is found it will skip switchIfEmpty() and map the response
        return customerRepository.findCustomerByUsername(username)
                .map(customerResponseMapper::mapFrom)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(CUSTOMER_NOT_FOUND)))
                .log();
    }

    public Flux<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll()
                .map(customerResponseMapper::mapFrom)
                .delayElements(Duration.ofSeconds(2)) //delay elements to see if the flux works
                .log();
    }

    public Flux<CustomerResponse> updateCustomerAddress(AddressRequest addressRequest, String username) {

        return customerRepository.findCustomerByUsername(username)
                .map(c -> setCustomerAddress(addressRequest, c))
                .flatMap(customerRepository::save)
                .map(customerResponseMapper::mapFrom)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(CUSTOMER_NOT_FOUND)))
                .log();
    }

    public Flux<Void> deleteCustomerByUsername(String username){
        return customerRepository.findCustomerByUsername(username)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer with username: " + username + " does not exists")))
                .flatMap(x -> customerRepository.deleteCustomerByUsername(x.getUsername()));
    }

    public Flux<CustomerResponse> updateCustomerWithBasketItems(Set<BasketItemRequest> basketItemsRequest, String username){

        return customerRepository.findCustomerByUsername(username)
                .map(c -> addBasketItemsToCustomer(basketItemsRequest,c))
                .flatMap(customerRepository::save)
                .map(customerResponseMapper::mapFrom)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(CUSTOMER_NOT_FOUND)))
                .log();

    }

    private Customer addBasketItemsToCustomer(Set<BasketItemRequest> basketItemsRequest, Customer c) {

        Set<BasketItem> basketItems = new HashSet<>();

        basketItemsRequest.stream()
                .map(basketItemRequestMapper::mapFrom)
                .forEach(basketItems::add);

        c.getBasketItems().addAll(basketItems);
        return c;
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
