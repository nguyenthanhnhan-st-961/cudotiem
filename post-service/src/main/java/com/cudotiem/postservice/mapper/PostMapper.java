package com.cudotiem.postservice.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.cudotiem.postservice.dto.PostDetailDto;
import com.cudotiem.postservice.dto.PostDto;
import com.cudotiem.postservice.entity.Category;
import com.cudotiem.postservice.entity.Post;
import com.cudotiem.postservice.payload.response.PostDetailResponse;

@Configuration
public class PostMapper {

	@Autowired
	ModelMapper mapper;
	
	public PostDto toPostDto(Post post) {
		return mapper.map(post, PostDto.class);
	}
	
	public PostDetailDto toPostDetailDto(Post post) {
		return mapper.map(post, PostDetailDto.class);
	}
	
	public Post toPost(PostDto postDto) {
		return mapper.map(postDto, Post.class);
	}
	
	public Post toPost(PostDetailDto postDetailDto) {
		return mapper.map(postDetailDto, Post.class);
	}
	
	public PostDetailResponse toDetailResponse(Post post) {
		List<String> imageUrls = new ArrayList<>();
		post.getImages().forEach(image -> imageUrls.add(image.getUrl()));
		
		PostDetailResponse postDetailResponse = new PostDetailResponse();
		postDetailResponse.setId(post.getId());
		postDetailResponse.setTitle(post.getTitle());
		postDetailResponse.setContent(post.getContent());
		postDetailResponse.setPrice(post.getPrice());
		postDetailResponse.setUrl(post.getUrl());
		postDetailResponse.setIdUser(post.getIdUser());
		postDetailResponse.setImageUrls(imageUrls);
		postDetailResponse.setDateCreated(null); 
		postDetailResponse.setDateUpdated(null); 
		postDetailResponse.setCategory(post.getCategory());
		
		return postDetailResponse;
	}
}
