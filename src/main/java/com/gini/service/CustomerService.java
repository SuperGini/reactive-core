package com.gini.service;

import com.gini.dto.request.CustomerRequest;
import com.gini.dto.response.CustomerResponse;
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
                            .map(customerResponseMapper::mapFrom);
    }
}
