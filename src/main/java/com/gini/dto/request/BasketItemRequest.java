package com.gini.dto.request;

import java.math.BigDecimal;


public record BasketItemRequest(
    String itemName,
    Integer itemsNumber,
    BigDecimal price
) {
}
