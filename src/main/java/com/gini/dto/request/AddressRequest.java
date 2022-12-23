package com.gini.dto.request;

public record AddressRequest(

    String street,
    String streetNumber,
    String town
) {
}
