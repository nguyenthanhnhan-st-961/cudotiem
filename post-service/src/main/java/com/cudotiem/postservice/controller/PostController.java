package com.cudotiem.postservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cudotiem.postservice.dto.PostDetailDto;
import com.cudotiem.postservice.dto.PostDto;
import com.cudotiem.postservice.payload.request.PostDetailRequest;
import com.cudotiem.postservice.payload.response.PostDetailResponse;
import com.cudotiem.postservice.payload.response.PostDetailUserResponse;
import com.cudotiem.postservice.service.IPostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/post")
public class PostController {

	@Autowired
	IPostService service;

	@GetMapping()
	public List<PostDto> getAllPosts() {
		return service.getAllPosts();
	}

	@GetMapping("/sort")
	public List<PostDto> getAllPostsWithSort(@RequestParam String field) {
		return service.getAllPostsWithSort(field);
	}

	@GetMapping("/pagination")
	public List<PostDto> getAllPostsWithPagination(@RequestParam int offset, @RequestParam int size) {
		return service.getPostsWithPagination(offset, size);
	}

	@GetMapping("/sort/pagination")
	public List<PostDto> getPostsWithPagination(@RequestParam String field, @RequestParam int offset,
			@RequestParam int size) {
		return service.getPostsWithPaginationAndSort(field, offset, size);
	}

//	@GetMapping("/filter")
//	public List<PostDto> filterPostsByPrice(@RequestParam double min, @RequestParam double max) {
//		return service.filterPostsByPrice(min, max);
//	}

	@GetMapping("/filter")
	List<PostDto> filterPostsByPriceAndPagination(@RequestParam double min, @RequestParam double max, @RequestParam int offset, @RequestParam int size, @RequestParam String field) {
		return service.filterPostsByPriceAndPagination(min, max, offset, size, field);
	}

//	@GetMapping("/search")
//	List<PostDto> searchPostsByTitle(@RequestParam String title) {
//		return service.searchPostsByTitle(title);
//	}

	@GetMapping("/search")
	List<PostDto> searchPostsByTitleAndPagination(@RequestParam String title, @RequestParam int offset, @RequestParam int size, @RequestParam String field) {
		return service.searchPostsByTitleAndPagination(title, offset, size, field);
	}

	@GetMapping("/{id}")
	public PostDetailUserResponse getPostById(@PathVariable Long id) {
		return service.getPostById(id);
	}

//	@GetMapping("/{url}")
//	public PostDetailUserResponse getPostByUrl(@RequestParam String url) {
//		return service.getPostByUrl(url);
//	}

	@PostMapping
	public Long createPost(@Valid @RequestBody PostDetailRequest request) {
		return service.createPost(request);
	}

//	@PutMapping("/{id}")
//	public PostDetailDto updatePost(@RequestParam Long id, @Valid @RequestBody PostDetailDto postDetailDto) {
//		return service.updatePostById(id, postDetailDto);
//	}

	@DeleteMapping
	public void deletePosts(@RequestBody List<Long> ids) {
		service.deletePosts(ids);
	}

	@GetMapping("/approve/{id}")
	public PostDetailResponse approve(@PathVariable Long id) {
		return service.approvePost(id);
	}
}
