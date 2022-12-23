package com.gini.mapper.response;

import com.gini.dto.response.AddressResponse;
import com.gini.mapper.Mapper;
import com.gini.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressResponseMapper implements Mapper<AddressResponse, Address> {


    @Override
    public AddressResponse mapFrom(Address address) {

        return new AddressResponse(
                address.getStreet(),
                address.getStreetNumber(),
                address.getTown()
        );
    }
}
