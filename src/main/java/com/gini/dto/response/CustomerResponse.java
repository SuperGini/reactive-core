package com.gini.dto.response;

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
public class CustomerResponse {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private AddressResponse address;
    private Set<BasketItemResponse> basketItems = new HashSet<>();
}
