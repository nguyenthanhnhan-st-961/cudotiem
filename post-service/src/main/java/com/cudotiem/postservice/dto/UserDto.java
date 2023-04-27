package com.cudotiem.postservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDto {
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
}
