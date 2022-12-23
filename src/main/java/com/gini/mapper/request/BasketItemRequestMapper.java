package com.gini.mapper.request;

import com.gini.dto.request.BasketItemRequest;
import com.gini.mapper.Mapper;
import com.gini.model.BasketItem;
import org.springframework.stereotype.Component;

@Component
public class BasketItemRequestMapper implements Mapper<BasketItem, BasketItemRequest> {


    @Override
    public BasketItem mapFrom(BasketItemRequest basketItemRequest) {

        return new BasketItem(
                basketItemRequest.itemName(),
                basketItemRequest.itemsNumber(),
                basketItemRequest.price()
        );
    }
}
