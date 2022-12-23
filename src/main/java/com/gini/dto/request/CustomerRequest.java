package com.gini.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private AddressRequest address;
    private Set<BasketItemRequest> basketItems = new HashSet<>();


}
