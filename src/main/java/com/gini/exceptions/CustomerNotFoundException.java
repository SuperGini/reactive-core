package com.gini.exceptions;

public class CustomerNotFoundException extends RuntimeException{

    //if you don't want to see the stack trace
    //https://stackoverflow.com/questions/31250598/prevent-stack-trace-logging-for-custom-exception-in-spring-boot-application
//    public CustomerNotFoundException(String message) {
//        super(message, null, false, false);
//    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
