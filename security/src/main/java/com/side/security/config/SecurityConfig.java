package com.side.security.config;

import static org.springframework.core.Ordered.*;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.domain.memory.service.MemoryService;
import com.side.security.filter.CustomCsrfFilter;
import com.side.security.filter.IdAndPasswordAuthenticationFilter;
import com.side.security.filter.SecurityFilterChainExceptionHandler;
import com.side.security.jwt.filter.JwtAuthenticationFilter;
import com.side.security.jwt.filter.JwtRefreshFilter;
import com.side.security.jwt.service.JwtService;
import com.side.security.service.SecurityService;
import com.side.security.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final ObjectMapper objectMapper;
	private final ResponseUtils responseUtils;
	private final JwtService jwtService;
	private final MemoryService memoryService;
	private final SecurityService securityService;

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomCsrfFilter csrfFilter;
	private final SecurityFilterChainExceptionHandler exceptionHandler;

	/**
	 * 공통 보안 설정 적용
	 */
	private HttpSecurity applyCommonSecurity(HttpSecurity http) throws Exception {

		return http.formLogin(AbstractHttpConfigurer::disable)
				   .httpBasic(AbstractHttpConfigurer::disable)
				   .csrf(AbstractHttpConfigurer::disable)
				   .cors(cors -> cors.configurationSource(corsConfigurationSource()))
				   .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				   .addFilterBefore(exceptionHandler, DisableEncodeUrlFilter.class);
	}

	/**
	 * 인증 필터 적용
	 */
	private HttpSecurity applyAuthenticationFilters(HttpSecurity http) {

		return http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				   .addFilterAfter(csrfFilter, JwtAuthenticationFilter.class);
	}

	@Bean
	@Order(HIGHEST_PRECEDENCE)
	public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {

		return applyCommonSecurity(http).securityMatcher("/swagger-*",
											"/*.png",
											"/index.css",
											"/swagger-ui/openapi3.yaml")
										.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
										.build();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain refreshFilterChain(HttpSecurity http) throws Exception {

		JwtRefreshFilter refreshFilter = new JwtRefreshFilter(
			jwtService,
			memoryService,
			securityService,
			responseUtils
		);

		return applyCommonSecurity(http).securityMatcher("/api/auth/refresh", "/swagger-ui")
										.addFilterBefore(refreshFilter, UsernamePasswordAuthenticationFilter.class)
										.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
										.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain loginFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws
		Exception {

		IdAndPasswordAuthenticationFilter loginFilter = new IdAndPasswordAuthenticationFilter(authenticationManager,
			objectMapper, responseUtils);

		return applyCommonSecurity(http).securityMatcher("/api/sign/in")
										.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
										.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
										.build();
	}

	@Bean
	@Order(3)
	public SecurityFilterChain generalSecurityFilterChain(HttpSecurity http) throws Exception {

		return applyAuthenticationFilters(applyCommonSecurity(http).securityMatcher("/**")).authorizeHttpRequests(
			auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated()).build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider(SecurityService securityService,
		PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(securityService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		// Origin 허용 패턴 (포트 와일드카드)
		config.setAllowedOriginPatterns(List.of("http://localhost:[*]", "http://127.0.0.1:[*]"));

		// 허용 메서드
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

		// 허용 헤더 (Content-Type 포함)
		config.setAllowedHeaders(List.of("*"));

		// 인증 허용
		config.setAllowCredentials(true);

		// 브라우저가 JavaScript로 접근할 수 있는 응답 헤더를 지정
		config.setExposedHeaders(List.of("Authorization", "Access-Control-Allow-*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return source;
	}

	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterRegistration(
		JwtAuthenticationFilter filter) {
		FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
		registration.setEnabled(false);
		return registration;
	}

	@Bean
	public FilterRegistrationBean<CustomCsrfFilter> customCsrfFilterRegistration(CustomCsrfFilter filter) {
		FilterRegistrationBean<CustomCsrfFilter> registration = new FilterRegistrationBean<>(filter);
		registration.setEnabled(false);
		return registration;
	}

	@Bean
	public FilterRegistrationBean<SecurityFilterChainExceptionHandler> securityFilterChainExceptionHandlerRegistration(
		SecurityFilterChainExceptionHandler filter) {
		FilterRegistrationBean<SecurityFilterChainExceptionHandler> registration = new FilterRegistrationBean<>(filter);
		registration.setEnabled(false);
		return registration;
	}
}