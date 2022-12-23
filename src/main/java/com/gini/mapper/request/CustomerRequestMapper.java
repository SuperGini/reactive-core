package com.gini.mapper.request;

import com.gini.dto.request.CustomerRequest;
import com.gini.mapper.Mapper;
import com.gini.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerRequestMapper implements Mapper<Customer, CustomerRequest> {

    private final BasketItemRequestMapper basketItemMapper;
    private final AddressRequestMapper addressMapper;


    @Override
    public Customer mapFrom(CustomerRequest customerRequest) {

        var address = addressMapper.mapFrom(customerRequest.getAddress());

        var basketItems = customerRequest.getBasketItems().stream()
                .map(basketItemMapper::mapFrom)
                .collect(Collectors.toSet());

        return Customer.builder()
                .username(customerRequest.getUsername())
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .address(address)
                .basketItems(basketItems)
                .build();
    }
}
