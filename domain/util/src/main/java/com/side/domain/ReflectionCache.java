package com.side.domain;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionCache<T> {

	private static final Map<String, Method> CACHED_METHOD = new ConcurrentHashMap<>();
	private static final Map<String, Field[]> CACHED_FILEDS = new ConcurrentHashMap<>();

	private ReflectionCache() {
	}

	public static void callMethod(String methodName, Object obj) {
		Method method = CACHED_METHOD.computeIfAbsent(methodName,
			name -> {
				try {
					return obj.getClass().getMethod(name);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				}
			});

		try {
			method.invoke(obj);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Optional<Field[]> getFields(Object obj) {

		// Bootstrap ClassLoader
		if (obj == null || obj.getClass().getClassLoader() == null) {
			return Optional.empty();
		}

		return Optional.of(CACHED_FILEDS.computeIfAbsent(obj.getClass().getSimpleName(),
			name -> obj.getClass().getDeclaredFields()));
	}
}
