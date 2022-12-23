package com.gini.repository;

import com.gini.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface CustomerRepository extends ReactiveMongoRepository<Customer, Integer> {

    Mono<Customer> findCustomerByUsername(String username);

}
