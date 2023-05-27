package com.cudotiem.postservice.service;

import java.util.List;

import com.cudotiem.postservice.dto.post.PostAdminDto;
import com.cudotiem.postservice.dto.post.PostApprovedDto;
import com.cudotiem.postservice.dto.post.PostDetailDto;
import com.cudotiem.postservice.dto.post.PostDto;
import com.cudotiem.postservice.dto.post.PostUserDto;
import com.cudotiem.postservice.entity.EStatus;
import com.cudotiem.postservice.payload.request.PostDetailRequest;
import com.cudotiem.postservice.payload.response.PostDetailResponse;
import com.cudotiem.postservice.payload.response.PostDetailUserResponse;
import com.cudotiem.postservice.payload.response.PostPaginationResponse;

public interface IPostService {
	
	List<PostDto> getAllPosts();
	
	PostPaginationResponse<PostAdminDto> getPostsAdmin(int offset, int size, String field);
	
	PostPaginationResponse<PostUserDto> getPostsByUsername(String username, int offset, int size, String field);
	
	List<PostDto> filterPostsByPrice(double min, double max);
	
	PostPaginationResponse<PostApprovedDto> filterPostsByPriceAndPagination(double min, double max, int offset, int size, String field);
	
	PostPaginationResponse<PostUserDto> getPostsByStatus(EStatus status, int offset, int size, String field);
	
	PostPaginationResponse<PostApprovedDto> searchByKeyword(String title, int offset, int size, String field);
	
	PostPaginationResponse<PostApprovedDto> getPostsApproved(int offset , int size, String field);
	
	PostDetailUserResponse getPostById(Long id);
	
	Long createPost(String token, PostDetailRequest postDetailRequest);
	
	PostDetailResponse handlePost(Long id, EStatus status);

	PostDetailResponse updatePostById(Long id, PostDetailDto postDetailDto);

	void deletePosts(List<Long> ids);

}
