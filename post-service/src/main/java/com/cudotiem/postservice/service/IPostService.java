package com.cudotiem.postservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.cudotiem.postservice.dto.PostDetailDto;
import com.cudotiem.postservice.dto.PostDto;
import com.cudotiem.postservice.entity.Post;
import com.cudotiem.postservice.payload.request.PostDetailRequest;
import com.cudotiem.postservice.payload.response.PostDetailResponse;
import com.cudotiem.postservice.payload.response.PostDetailUserResponse;

public interface IPostService {
	
	List<PostDto> getAllPosts();
	
	List<PostDto> getAllPostsWithSort(String field);
	
	List<PostDto> getPostsWithPagination(int offset, int size);
	
	List<PostDto> getPostsWithPaginationAndSort(String field, int offset, int size);

	List<PostDto> filterPostsByPrice(double min, double max);
	
	List<PostDto> filterPostsByPriceAndPagination(double min, double max, int offset, int size, String field);
	
	List<PostDto> searchPostsByTitle(String title);
	
	List<PostDto> searchPostsByTitleAndPagination(String title, int offset, int size, String field);
	
	PostDetailUserResponse getPostById(Long id);
	
//	PostDetailUserResponse getPostByUrl(String url);

	Long createPost(PostDetailRequest request);
	
	PostDetailResponse approvePost(Long id);

//	PostDetailDto updatePostById(Long id, PostDetailDto postDetailDto);

	void deletePosts(List<Long> ids);

}
