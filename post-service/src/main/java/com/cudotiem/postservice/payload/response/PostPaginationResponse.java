package com.cudotiem.postservice.payload.response;

import java.util.List;

import com.cudotiem.postservice.dto.PostDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPaginationResponse {

	private List<PostDto> paginationPosts;
	private Integer totalPage;
}
