package com.cudotiem.postservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cudotiem.postservice.dto.UserDto;

@FeignClient(value = "account-service", url = "http://localhost:8081")
public interface APIClient {
	 @GetMapping(value = "/api/profile/user-info/{id}")
	    UserDto getUserById(@PathVariable("id") Long idUser);
}
