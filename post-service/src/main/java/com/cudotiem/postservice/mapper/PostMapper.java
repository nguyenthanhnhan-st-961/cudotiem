package com.cudotiem.postservice.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import com.cudotiem.postservice.dto.post.PostAdminDto;
import com.cudotiem.postservice.dto.post.PostApprovedDto;
import com.cudotiem.postservice.dto.post.PostDetailDto;
import com.cudotiem.postservice.dto.post.PostDto;
import com.cudotiem.postservice.dto.post.PostUserDto;
import com.cudotiem.postservice.entity.Post;
import com.cudotiem.postservice.payload.response.PostDetailResponse;

@Configuration
public class PostMapper {

	@Autowired
	ModelMapper mapper;

	@Autowired
	MessageSource messageSource;

	public PostDto toPostDto(Post post) {
		PostDto result = new PostDto();

		LocalDateTime localDateTime = post.getDatePosted();
		long milliseconds = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

		result.setId(post.getId());
		result.setTitle(post.getTitle());
		result.setPrice(post.getPrice());
		result.setSlug(post.getSlug());
		result.setThumbnail(post.getImages().get(0).getUrl());
		result.setDatePosted(milliseconds);
		return result;
	}

	public PostApprovedDto toPostApprovedDto(Post post) {
		PostApprovedDto result = new PostApprovedDto();

		LocalDateTime localDateTime = post.getDatePosted();
		long milliseconds = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

		result.setId(post.getId());
		result.setTitle(post.getTitle());
		result.setPrice(post.getPrice());
		result.setSlug(post.getSlug());
		result.setThumbnail(post.getImages().get(0).getUrl());
		result.setDatePosted(milliseconds);
		return result;
	}

	public PostUserDto toPostUserDto(Post post) {
		PostUserDto result = new PostUserDto();
		result.setId(post.getId());
		result.setTitle(post.getTitle());
		result.setPrice(post.getPrice());
		result.setSlug(post.getSlug());
		result.setThumbnail(post.getImages().get(0).getUrl());
		result.setDatePosted(post.getDatePosted().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		result.setDateCreated(post.getDateCreated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		result.setStatus(post.getStatus().toString());
		return result;
	}

	public PostAdminDto toPostAdminDto(Post post) {
		PostAdminDto result = new PostAdminDto();
		result.setId(post.getId());
		result.setTitle(post.getTitle());
		result.setPrice(post.getPrice());
		result.setSlug(post.getSlug());
		result.setThumbnail(post.getImages().get(0).getUrl());
		result.setDatePosted(post.getDatePosted().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		result.setDateCreated(post.getDateCreated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		result.setDateUpdated(post.getDateUpdated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		result.setUsername(post.getUsername());
		result.setStatus(post.getStatus().toString());
		return result;
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
		postDetailResponse.setSlug(post.getSlug());
		postDetailResponse.setUsername(post.getUsername());
		postDetailResponse.setImageUrls(imageUrls);
		postDetailResponse
				.setDatePosted(post.getDatePosted().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		postDetailResponse.setCategoryName(post.getCategory().getName());

		return postDetailResponse;
	}
}
