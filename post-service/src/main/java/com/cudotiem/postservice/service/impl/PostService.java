package com.cudotiem.postservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cudotiem.postservice.dto.PostDetailDto;
import com.cudotiem.postservice.dto.PostDto;
import com.cudotiem.postservice.dto.UserDto;
import com.cudotiem.postservice.entity.Category;
import com.cudotiem.postservice.entity.EStatus;
import com.cudotiem.postservice.entity.Image;
import com.cudotiem.postservice.entity.Post;
import com.cudotiem.postservice.mapper.ImageMapper;
import com.cudotiem.postservice.mapper.PostMapper;
import com.cudotiem.postservice.payload.request.PostDetailRequest;
import com.cudotiem.postservice.payload.response.PostDetailResponse;
import com.cudotiem.postservice.payload.response.PostDetailUserResponse;
import com.cudotiem.postservice.repository.CategoryRepository;
import com.cudotiem.postservice.repository.PostRepository;
import com.cudotiem.postservice.service.APIClient;
import com.cudotiem.postservice.service.ICategoryService;
import com.cudotiem.postservice.service.IImageService;
import com.cudotiem.postservice.service.IPostService;

@Service
public class PostService implements IPostService {

	@Autowired
	PostRepository postRepository;

	@Autowired
	ICategoryService categoryService;

	@Autowired
	IImageService imageService;

	@Autowired
	PostMapper postMapper;

	@Autowired
	ImageMapper imageMapper;

	@Autowired
	APIClient apiClient;

	@Override
	public List<PostDto> getAllPosts() {
		return postRepository.findAll().stream().map(post -> postMapper.toPostDto(post)).collect(Collectors.toList());

	}

	@Override
	public PostDetailUserResponse getPostById(Long id) {

		PostDetailUserResponse postDetailUserResponse = new PostDetailUserResponse();

		Post post = postRepository.findById(id).get();
		PostDetailResponse postDetailResponse = postMapper.toDetailResponse(post);
		UserDto userDto = apiClient.getUserById(post.getIdUser());

		postDetailUserResponse.setPostDetailResponse(postDetailResponse);
		postDetailUserResponse.setUserDto(userDto);

		return postDetailUserResponse;

	}

//	@Override
//	public PostDetailUserResponse getPostByUrl(String url) {
//		
//	}

	@Override
	public Long createPost(PostDetailRequest request) {

		Post post = new Post();
		Category category = categoryService.getCategoryById(request.getIdCategory());

		List<String> images = request.getImageUrls();

		StringBuilder url = new StringBuilder(category.getName());
		String randomString = UUID.randomUUID().toString();
		url.append(randomString);
//		List<Image> images = request.getImageUrls().stream().map(imageUrl -> imageService.getImageByUrl(imageUrl)).collect(Collectors.toList());

		post.setTitle(request.getTitle());
		post.setContent(request.getContent());
		post.setStatus(EStatus.PENDING);
		post.setPrice(request.getPrice());
		post.setUrl(url.toString());
		post.setIdUser(request.getIdUser());
		post.setCategory(category);

		Post createdPost = postRepository.save(post);
		imageService.uploadImages(images, createdPost);

		return post.getId();
	}
	
	public PostDetailResponse approvePost(Long id) {
		Post post = postRepository.findById(id).get();
		
		if(post == null) return null;
		
		post.setStatus(EStatus.APPROVED);
		postRepository.save(post);
		
		return postMapper.toDetailResponse(post);
	}

//	@Override
//	public PostDetailDto updatePostById(Long id, PostDetailDto postDetailDto) {
//		Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found by id"));
//		List<Image> images = new ArrayList<>();
//		List<ImageDto> imageDtos = postDetailDto.getImageNames().stream()
//				.map(name -> imageService.getImage(name)).collect(Collectors.toList());
//		images = imageDtos.stream().map(imageDto -> imageMapper.toImage(imageDto)).collect(Collectors.toList());
//
//		Category category = categoryRepository.findById(postDetailDto.getId_category()).get();
//
//		post.setTitle(postDetailDto.getTitle());
//		post.setContent(postDetailDto.getContent());
//		post.setActive(postDetailDto.isActive());
//		post.setPrice(postDetailDto.getPrice());
//		post.setUrl(postDetailDto.getUrl());
//		post.setIdUser(postDetailDto.getIdUser());
//		post.setImages(images);
//		post.setDateUpdated(LocalDateTime.now());
//		post.setCategory(category);
//		return postMapper.toPostDetailDto(post);
//	}

	@Override
	public void deletePosts(List<Long> ids) {

		for (Long id : ids) {
			Post post = postRepository.findById(id).get();
			post.setStatus(EStatus.HIDDEN);
		}

	}

}
