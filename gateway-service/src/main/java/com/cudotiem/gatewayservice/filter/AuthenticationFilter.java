package com.cudotiem.gatewayservice.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	public static class Config {
	}

	@Autowired
	RouteValidator validator;

	@Autowired
//	RestTemplate restTemplate;
	WebClient webClient;

	public AuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			
			if(validator.isSecured.test(request)) {
				if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("missing authorization header");
				}
				
				String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				
				String[] tokens = authHeader.split(" ");
				
				if(tokens.length != 2 || !tokens[0].equals("Bearer")) {
					throw new RuntimeException("Incorrect authorization header");
				}
				
				if(authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7);
				}
				
				try {
					  webClient.get()
				                 .uri("http://localhost:8083/api/v1/auth/validate?token=" + tokens[1])
				                         .retrieve()
				                                 .bodyToMono(String.class);
				} catch (Exception e) {
					System.out.println("invalid access...!");
					throw new RuntimeException("unauthorization");
				}
			}
			return chain.filter(exchange);
		});
	}
}
