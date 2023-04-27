package com.cudotiem.postservice.service;

import java.util.List;
import java.util.Optional;

import com.cudotiem.postservice.dto.PostDetailDto;
import com.cudotiem.postservice.dto.PostDto;
import com.cudotiem.postservice.entity.Post;
import com.cudotiem.postservice.payload.request.PostDetailRequest;
import com.cudotiem.postservice.payload.response.PostDetailResponse;
import com.cudotiem.postservice.payload.response.PostDetailUserResponse;

public interface IPostService {
	
	List<PostDto> getAllPosts();

	PostDetailUserResponse getPostById(Long id);
	
//	PostDetailUserResponse getPostByUrl(String url);

	Long createPost(PostDetailRequest request);
	
	PostDetailResponse approvePost(Long id);

//	PostDetailDto updatePostById(Long id, PostDetailDto postDetailDto);

	void deletePosts(List<Long> ids);
}
