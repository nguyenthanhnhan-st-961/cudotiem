package com.cudotiem.postservice.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostUserDto extends PostDto{

	private String status;
	private Long dateCreated;
}
