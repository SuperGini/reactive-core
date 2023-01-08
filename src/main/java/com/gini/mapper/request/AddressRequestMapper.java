package com.gini.mapper.request;

import com.gini.dto.request.AddressRequest;
import com.gini.mapper.Mapper;
import com.gini.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressRequestMapper implements Mapper<Address, AddressRequest> {


    @Override
    public Address mapFrom(AddressRequest addressRequest) {

        if(addressRequest == null) return null;

        return new Address(
                addressRequest.street(),
                addressRequest.streetNumber(),
                addressRequest.town()
        );
    }
}
