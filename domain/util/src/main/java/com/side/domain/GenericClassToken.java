package com.side.domain;

import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Getter
public abstract class GenericClassToken<T> {
    private final Class<T> rawType;

    @SuppressWarnings("unchecked")
    public GenericClassToken() {
        Type superClass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        this.rawType = (Class<T>) (type instanceof Class ? type : ((ParameterizedType) type).getRawType());
    }
}