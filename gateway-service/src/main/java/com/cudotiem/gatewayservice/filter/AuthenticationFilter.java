package com.cudotiem.gatewayservice.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{
	
	public static class Config{}

	@Autowired
	RouteValidator validator;
	
	@Autowired
	RestTemplate restTemplate;

	public AuthenticationFilter() {
		super(Config.class); 
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			if(validator.isSecured.test(exchange.getRequest())) {
				if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("missing authorization header");
				}
				
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				
				if(authHeader != null && authHeader.startsWith("Bearner ")) {
					authHeader = authHeader.substring(7);
				}
				
				try {
					restTemplate.getForObject("http://auth-service//validate?token" + authHeader, String.class);
				} catch (Exception e) {
					System.out.println("invalid access...!");
					throw new RuntimeException("unauthorization");
				}
			}
			return chain.filter(exchange);
		});
	}
}
