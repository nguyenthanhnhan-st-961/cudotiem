package com.cudotiem.postservice.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

	@Autowired
	MessageSource messageSource;
	
	// testing api
	@GetMapping("/post/greeting")
	public String getGreeting(@RequestHeader(value = "Accept-Language", required = false) Locale locale) {
	    return messageSource.getMessage("category", null, locale);
	}

// admin api
	// hiển thị danh sách tất cả bài viết ở trang admin(PostAdminDto)
	@GetMapping("/admin/post")
	ResponseEntity<PostPaginationResponse<PostAdminDto>> getAllPostsAdmin(@RequestHeader(value = "Accept-Language", required = false) Locale locale, @RequestParam(required = false) EStatus status, @RequestParam int offset,
			@RequestParam int size, @RequestParam(defaultValue = "dateCreated") String field) {
		return ResponseEntity.ok().body(service.getPostsAdmin(locale, status, offset, size, field));
	}

	// admin approve or reject the request create post
	@PutMapping("/admin/post/{id}")
	public PostDetailResponse handlePost(@PathVariable Long id, @RequestParam EStatus status) {
		return service.handlePost(id, status);
	}
	
	// admin approve or reject the request update post
	@PutMapping("/admin/post/update-approved/{id}")
	public PostDetailResponse updateApproved(@PathVariable Long id, @RequestParam EStatus status) {
		return service.updateApproved(id, status);
	}

// user api
	// hiển thị danh sách tất cả bài viết(PostUserDto)
	@GetMapping("/user/post")
	ResponseEntity<PostPaginationResponse<PostUserDto>> getAllPostsByUsername(
			@RequestHeader(name = "Authorization") String token, @RequestParam(required = false) EStatus status ,@RequestParam int offset, @RequestParam int size,
			@RequestParam(defaultValue = "dateCreated") String field) {
		token = token.split(" ")[1];
		
//		if(status != null) {
//			return  ResponseEntity.ok().body(service.getPostsByStatus(status, offset, size, field));
//		}
		return ResponseEntity.ok().body(service.getPostsByUsername(token, status, offset, size, field));
	}

	// get posts by status
//	@GetMapping("/user/post/get-by-status")
//	PostPaginationResponse<PostUserDto> getPostsByStatus(@RequestParam int offset,
//			@RequestParam int size, @RequestParam(defaultValue = "datePosted") String field) {
//		return service.getPostsByStatus(status, offset, size, field);
//	}
	
	// user send new post and wait for approve
	@PostMapping("/user/post")
	public ResponseEntity<?> createPost(@RequestHeader(name = "Authorization") String token,
			@Valid @RequestBody PostDetailRequest postDetailRequest) {
		token = token.split(" ")[1];
		return service.createPost(token, postDetailRequest) != null ? ResponseEntity.ok().body("successful") : ResponseEntity.badRequest().body("failed");
	}

	// user send request to update post
	@PutMapping("/user/post/{id}")
	public ResponseEntity<?> updatePost(@RequestHeader(name = "Authorization") String token, @PathVariable Long id, @RequestParam(required = false) EStatus status, @Valid @RequestBody(required = false) PostDetailRequest postDetailRequest) {
		token = token.split(" ")[1];
		return service.updatePostById(id, token, status, postDetailRequest) != null ? ResponseEntity.ok().body("sent") : ResponseEntity.badRequest().body("failed");
	}

	// user hide the post
	@DeleteMapping("/user/post")
	public void deletePosts(@RequestBody List<Long> ids) {
		service.deletePosts(ids);
	}

// guest api
	// guest get posts approved
	@GetMapping("/post")
	public PostPaginationResponse<PostApprovedDto> getPostsApproved(@RequestParam(required = false) String categoryCode, @RequestParam int offset, @RequestParam int size,
			@RequestParam(defaultValue = "datePosted") String field) {
		return service.getPostsApproved(categoryCode, offset, size, field);
	}

	// lọc bài viết theo khoảng giá
	@GetMapping("/post/filter")
	PostPaginationResponse<PostApprovedDto> filterPostsByPriceAndPagination(@RequestParam double min,
			@RequestParam double max, @RequestParam int offset, @RequestParam int size,
			@RequestParam(defaultValue = "datePosted") String field) {
		return service.filterPostsByPriceAndPagination(min, max, offset, size, field);
	}

	// tìm kiếm bài viết
	@GetMapping("/post/search")
	PostPaginationResponse<PostApprovedDto> searchPostsByTitleAndPagination(@RequestParam String keyword,
			@RequestParam int offset, @RequestParam int size, @RequestParam(defaultValue = "datePosted") String field) {
		return service.searchByKeyword(keyword, offset, size, field);
	}

	// guest get post by id
	@GetMapping("/post/{id}")
	public PostDetailUserResponse getPostById(@RequestHeader(value = "Accept-Language", required = false) Locale locale, @PathVariable Long id) {
		return service.getPostById(id, locale);
	}

	@GetMapping("/post/status")
	public List<EStatus> getAllStatus() {
		return service.getAllStatus();
	}
}
