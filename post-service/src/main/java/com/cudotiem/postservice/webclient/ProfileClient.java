package com.cudotiem.postservice.webclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cudotiem.postservice.dto.UserDto;

@FeignClient(value = "account-service", url = "http://localhost:8081")
public interface ProfileClient {
	 @GetMapping(value = "/api/v1/profile/user-info/{username}")
	    UserDto getUserByUsername(@PathVariable("username") String username);
}
