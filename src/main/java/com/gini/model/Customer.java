package com.gini.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("customer")
public class Customer {

    @Id
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private Address address;
    private Set<BasketItem> basketItems = new HashSet<>();


}
