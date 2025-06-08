package com.side.domain;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExternalApiException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -2950545099839156527L;

	private static final String DEFAULT_MESSAGE = "Internal Server Error";

	private final int statusCode;
	private final String message;

	public ExternalApiException(Exception e) {
		this.statusCode = 500;
		this.message = e.getMessage();
	}

	public ExternalApiException(int statusCode) {
		this.statusCode = statusCode;
		this.message = DEFAULT_MESSAGE;
	}

	public ExternalApiException(String message) {
		this.statusCode = 400;
		this.message = message;
	}

}
