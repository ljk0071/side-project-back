package com.side.domain;

import java.util.Map;

public interface HttpRequest {

    <O, T> T get(String url, Map<String, String> headers, O data, Class<T> returnType);

    <O, T> T post(String url, Map<String, String> headers, O data, Class<T> returnType);

    <O, T> T patch(String url, Map<String, String> headers, O data, Class<T> returnType);

    <O, T> T put(String url, Map<String, String> headers, O data, Class<T> returnType);

    <O, T> T delete(String url, Map<String, String> headers, O data, Class<T> returnType);
}
