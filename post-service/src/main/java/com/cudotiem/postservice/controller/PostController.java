package com.cudotiem.postservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cudotiem.postservice.dto.post.PostAdminDto;
import com.cudotiem.postservice.dto.post.PostApprovedDto;
import com.cudotiem.postservice.dto.post.PostDetailDto;
import com.cudotiem.postservice.dto.post.PostUserDto;
import com.cudotiem.postservice.entity.EStatus;
import com.cudotiem.postservice.payload.request.PostDetailRequest;
import com.cudotiem.postservice.payload.response.PostDetailResponse;
import com.cudotiem.postservice.payload.response.PostDetailUserResponse;
import com.cudotiem.postservice.payload.response.PostPaginationResponse;
import com.cudotiem.postservice.service.IPostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class PostController {

	@Autowired
	IPostService service;

//	@GetMapping
//	public List<PostDto> getAllPosts() {
//		return service.getAllPosts();
//	}

	// hiển thị danh sách tất cả bài viết ở trang admin(PostAdminDto)
	@GetMapping("/admin/post")
	ResponseEntity<PostPaginationResponse<PostAdminDto>> getAllPostsAdmin(@RequestParam int offset,
			@RequestParam int size, @RequestParam(defaultValue = "dateCreated") String field) {
		return ResponseEntity.ok().body(service.getPostsAdmin(offset, size, field));
	}
	

	@PutMapping("/admin/post/{id}")
	public PostDetailResponse handlePost(@PathVariable Long id, @RequestParam EStatus status) {
		return service.handlePost(id, status);
	}

	// hiển thị danh sách tất cả bài viết(PostUserDto)
	@GetMapping("/user/post")
	ResponseEntity<PostPaginationResponse<PostUserDto>> getAllPostsByUsername(
			@RequestHeader(name = "Authorization") String token, @RequestParam int offset, @RequestParam int size,
			@RequestParam(defaultValue = "dateCreated") String field) {
		token = token.split(" ")[1];
		return ResponseEntity.ok().body(service.getPostsByUsername(token, offset, size, field));
	}

	@GetMapping("/user/post/get-by-status")
	PostPaginationResponse<PostUserDto> getPostsByStatus(@RequestParam EStatus status, @RequestParam int offset,
			@RequestParam int size, @RequestParam(defaultValue = "datePosted") String field) {
		return service.getPostsByStatus(status, offset, size, field);
	}
	

	@PutMapping("/user/post/{id}")
	public PostDetailResponse updatePost(@RequestParam Long id, @Valid @RequestBody PostDetailDto postDetailDto) {
		return service.updatePostById(id, postDetailDto);
	}

	@DeleteMapping("/user/post")
	public void deletePosts(@RequestBody List<Long> ids) {
		service.deletePosts(ids);
	}

	@GetMapping("/post")
	public PostPaginationResponse<PostApprovedDto> getPostsApproved(@RequestParam int offset,
			@RequestParam int size, @RequestParam(defaultValue = "datePosted") String field) {
		return service.getPostsApproved(offset, size, field);
	}

	// lọc bài viết theo khoảng giá
	@GetMapping("/post/filter")
	PostPaginationResponse<PostApprovedDto> filterPostsByPriceAndPagination(@RequestParam double min,
			@RequestParam double max, @RequestParam int offset, @RequestParam int size, @RequestParam(defaultValue = "datePosted") String field) {
		return service.filterPostsByPriceAndPagination(min, max, offset, size, field);
	}

	// tìm kiếm bài viết
	@GetMapping("/post/search")
	PostPaginationResponse<PostApprovedDto> searchPostsByTitleAndPagination(@RequestParam String keyword,
			@RequestParam int offset, @RequestParam int size, @RequestParam(defaultValue = "datePosted") String field) {
		return service.searchByKeyword(keyword, offset, size, field);
	}

	@GetMapping("/post/{id}")
	public PostDetailUserResponse getPostById(@PathVariable Long id) {
		return service.getPostById(id);
	}

	@PostMapping("/post")
	public Long createPost(@RequestHeader(name = "Authorization") String token,
			@Valid @RequestBody PostDetailRequest postDetailRequest) {
		token = token.split(" ")[1];
		return service.createPost(token, postDetailRequest);
	}

}
