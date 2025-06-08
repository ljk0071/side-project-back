package com.side.rest.advice;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

	private final RestClient restClient = RestClient.builder().build();

	private final Environment env;

	private void sendErrorLogAsyncWebhookIfNotLocal(Exception e) {
		CompletableFuture.runAsync(() -> {

							 String url = "webhookUrl";
							 try {
								 URI uri = new URI(url);
								 Map<String, Object> map = new HashMap<>();
								 map.put("name", e.getStackTrace());
								 restClient.post().uri(uri).body(map).retrieve();
							 } catch (URISyntaxException ex) {
								 log.error("teams.webhook.url이 uri 문법에 맞지 않습니다. {}", url);
							 }

						 })

						 .exceptionally(ex -> {
							 if (ex != null) {
								 log.error("예상치 못한 에러 발생: ", ex);
							 }
							 return null;
						 });
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleCustomException(Exception e) {

		log.error("예상치 못한 에러 발생", e);

		if (!env.matchesProfiles("local")) {
			sendErrorLogAsyncWebhookIfNotLocal(e);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
	}
}