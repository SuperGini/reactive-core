package com.gini.mapper;

public interface Mapper<T, U> {

    T mapFrom(U object);

}
