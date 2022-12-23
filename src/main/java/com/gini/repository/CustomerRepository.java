package com.gini.repository;

import com.gini.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface CustomerRepository extends ReactiveMongoRepository<Customer, Integer> {
}
