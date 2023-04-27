package com.cudotiem.gatewayservice.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


@Component
public class RouteValidator {
	
	public static final List<String> openApiEndpoints = List.of(
			"/api/auth/register",
			"/api/auth/token",
			"/api/auth/test/all",
			"/api/auth/test/user",
			"/api/auth/test/mod",
			"/api/auth/signin",
			"/api/auth/signup",
			"/api/auth/signout",
			"/eureka"
			);
	
	public Predicate<ServerHttpRequest> isSecured = 
			request -> openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
}
