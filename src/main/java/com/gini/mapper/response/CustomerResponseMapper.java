package com.gini.mapper.response;

import com.gini.dto.response.BasketItemResponse;
import com.gini.dto.response.CustomerResponse;
import com.gini.mapper.Mapper;
import com.gini.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerResponseMapper implements Mapper<CustomerResponse, Customer> {

    private final AddressResponseMapper addressResponseMapper;
    private final BasketItemResponseMapper basketItemResponseMapper;

    @Override
    public CustomerResponse mapFrom(Customer customer) {

        var address = addressResponseMapper.mapFrom(customer.getAddress());

        Set<BasketItemResponse> basketItems = new HashSet<>();

        if(!customer.getBasketItems().isEmpty()){
            customer.getBasketItems().stream()
                    .map(basketItemResponseMapper::mapFrom)
                    .forEach(basketItems::add);
        }

        return CustomerResponse.builder()
                .id(customer.getId())
                .username(customer.getUsername())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .address(address)
                .basketItems(basketItems)
                .build();
    }
}
