package com.gini.exceptions;

public class CustomerAlreadyExists  extends RuntimeException{

    public CustomerAlreadyExists(String message) {
        super(message);
    }
}
