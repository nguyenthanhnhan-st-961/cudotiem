package com.cudotiem.postservice.payload.response;

import com.cudotiem.postservice.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDetailUserResponse {

	private UserDto userDto;
	private PostDetailResponse postDetailResponse;
}
