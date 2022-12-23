package com.gini.mapper.response;

import com.gini.dto.response.BasketItemResponse;
import com.gini.mapper.Mapper;
import com.gini.model.BasketItem;
import org.springframework.stereotype.Component;

@Component
public class BasketItemResponseMapper implements Mapper<BasketItemResponse, BasketItem> {


    @Override
    public BasketItemResponse mapFrom(BasketItem basketItem) {

        return new BasketItemResponse(
                basketItem.getItemName(),
                basketItem.getItemsNumber(),
                basketItem.getPrice()
        );
    }
}
