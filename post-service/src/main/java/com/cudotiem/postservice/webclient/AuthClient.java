package com.cudotiem.postservice.webclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "auth-service", url = "http://localhost:8083")
public interface AuthClient {
	@GetMapping(value = "/api/v1/auth/get-username/{token}")
	String getUsername(@PathVariable String token);
}
