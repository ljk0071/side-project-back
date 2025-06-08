package com.side.domain;

public interface HttpRequest<O, T> {

	void get(String url, O data);

	T get(String url, O data, Class<T> returnType);

	void post(String url, O data);

	T post(String url, O data, Class<T> returnType);

	void patch(String url, O data);

	void put(String url, O data);

	void delete(String url, O data);
}
