package com.gini.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BasketItem {

    private String itemName;
    private Integer itemsNumber;
    private BigDecimal price;
    private Customer customer;

}
