package com.gini.repository;

import com.gini.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CustomerRepository extends ReactiveMongoRepository<Customer, Integer> {

    Flux<Customer> findCustomerByUsername(String username);

    Mono<Void> deleteCustomerByUsername(String username);

}
