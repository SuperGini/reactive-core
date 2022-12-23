package com.gini.dto.response;

import java.math.BigDecimal;

public record BasketItemResponse (
     String itemName,
     Integer itemsNumber,
     BigDecimal price
){
}
