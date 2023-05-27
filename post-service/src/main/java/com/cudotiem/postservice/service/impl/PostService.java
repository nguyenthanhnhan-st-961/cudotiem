package com.cudotiem.postservice.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cudotiem.postservice.dto.UserDto;
import com.cudotiem.postservice.dto.post.PostAdminDto;
import com.cudotiem.postservice.dto.post.PostApprovedDto;
import com.cudotiem.postservice.dto.post.PostDetailDto;
import com.cudotiem.postservice.dto.post.PostDto;
import com.cudotiem.postservice.dto.post.PostUserDto;
import com.cudotiem.postservice.entity.Category;
import com.cudotiem.postservice.entity.EStatus;
import com.cudotiem.postservice.entity.Image;
import com.cudotiem.postservice.entity.Post;
import com.cudotiem.postservice.mapper.ImageMapper;
import com.cudotiem.postservice.mapper.PostMapper;
import com.cudotiem.postservice.payload.request.PostDetailRequest;
import com.cudotiem.postservice.payload.response.PostDetailResponse;
import com.cudotiem.postservice.payload.response.PostDetailUserResponse;
import com.cudotiem.postservice.payload.response.PostPaginationResponse;
import com.cudotiem.postservice.repository.CategoryRepository;
import com.cudotiem.postservice.repository.PostRepository;
import com.cudotiem.postservice.service.IImageService;
import com.cudotiem.postservice.service.IPostService;
import com.cudotiem.postservice.webclient.AuthClient;
import com.cudotiem.postservice.webclient.ProfileClient;

@Service
public class PostService implements IPostService {

	@Autowired
	PostRepository postRepository;

	@Autowired
//	ICategoryService categoryService;
	CategoryRepository categoryRepository;

	@Autowired
	IImageService imageService;

	@Autowired
	PostMapper postMapper;

	@Autowired
	ImageMapper imageMapper;

	@Autowired
	ProfileClient profileClient;

	@Autowired
	AuthClient authClient;

	@Override
	public List<PostDto> getAllPosts() {
		return postRepository.findAll().stream().map(post -> postMapper.toPostDto(post)).collect(Collectors.toList());

	}

	@Override
	public PostPaginationResponse<PostAdminDto> getPostsAdmin(int offset, int size, String field) {
		PostPaginationResponse<PostAdminDto> result = new PostPaginationResponse<>();
		Integer totalItem = postRepository.findAll().size();
		Integer totalPage = totalItem / size;
		List<PostAdminDto> listPostDto = postRepository
				.findAll(PageRequest.of(offset - 1, size).withSort(Sort.by(field))).stream()
				.map(post -> postMapper.toPostAdminDto(post)).collect(Collectors.toList());
		result.setPaginationPosts(listPostDto);
		result.setTotalPage(totalItem % size == 0 ? totalPage : totalPage + 1);
		return result;
	}

	@Override
	public PostPaginationResponse<PostUserDto> getPostsByUsername(String token, int offset, int size, String field) {
		PostPaginationResponse<PostUserDto> result = new PostPaginationResponse<>();
		// get username
		String username = authClient.getUsername(token);
		
		Integer totalItem = postRepository.findAllByUsernameLike(username).size();
		Integer totalPage = totalItem / size;
		List<PostUserDto> listPostDto = postRepository
				.findAllByUsernameLike(username, PageRequest.of(offset - 1, size).withSort(Sort.by(field))).stream()
				.map(post -> postMapper.toPostUserDto(post)).collect(Collectors.toList());
		result.setPaginationPosts(listPostDto);
		result.setTotalPage(totalItem % size == 0 ? totalPage : totalPage + 1);
		return result;
	}

	@Override
	public List<PostDto> filterPostsByPrice(double min, double max) {
		return postRepository.findByPriceBetween(min, max).stream().map(post -> postMapper.toPostDto(post))
				.collect(Collectors.toList());
	}

	@Override
	public PostPaginationResponse<PostApprovedDto> filterPostsByPriceAndPagination(double min, double max, int offset, int size,
			String field) {
		PostPaginationResponse<PostApprovedDto> result = new PostPaginationResponse<>();
		Integer totalItem = postRepository.findByPriceBetween(min, max).size();
		Integer totalPage = totalItem / size;
		List<PostApprovedDto> listPostDto = postRepository
				.findByPricePagination(min, max, PageRequest.of(offset - 1, size).withSort(Sort.by(field))).stream()
				.map(post -> postMapper.toPostApprovedDto(post)).collect(Collectors.toList());
		result.setPaginationPosts(listPostDto);
		result.setTotalPage(totalItem % size == 0 ? totalPage : totalPage + 1);
		return result;
	}

	@Override
	public PostPaginationResponse<PostUserDto> getPostsByStatus(EStatus status, int offset, int size, String field) {
		PostPaginationResponse<PostUserDto> result = new PostPaginationResponse<>();
		Integer totalItem = postRepository.findAllByStatus(status).size();
		Integer totalPage = totalItem / size;
		List<PostUserDto> listPostDto = postRepository
				.findAllByStatus(status, PageRequest.of(offset - 1, size).withSort(Sort.by(field))).stream()
				.map(post -> postMapper.toPostUserDto(post)).collect(Collectors.toList());
		result.setPaginationPosts(listPostDto);
		result.setTotalPage(totalItem % size == 0 ? totalPage : totalPage + 1);
		return result;
	}

	@Override
	public PostPaginationResponse<PostApprovedDto> searchByKeyword(String keyword, int offset, int size, String field) {
		PostPaginationResponse<PostApprovedDto> result = new PostPaginationResponse<>();
		Integer totalItem = postRepository.searchByKeyword(keyword).size();
		Integer totalPage = totalItem / size;
		List<PostApprovedDto> listPostDto = postRepository
				.searchByKeyword(keyword, PageRequest.of(offset - 1, size).withSort(Sort.by(field))).stream()
				.map(post -> postMapper.toPostApprovedDto(post)).collect(Collectors.toList());
		result.setPaginationPosts(listPostDto);
		result.setTotalPage(totalItem % size == 0 ? totalPage : totalPage + 1);
		return result;
	}

	@Override
	public PostPaginationResponse<PostApprovedDto> getPostsApproved(int offset, int size, String field) {

		PostPaginationResponse<PostApprovedDto> result = new PostPaginationResponse<>();
		Integer totalItem = postRepository.findAllByStatusEqualAprroved().size();
		Integer totalPage = totalItem / size;
		List<PostApprovedDto> listPostDto = postRepository
				.findAllByStatusEqualAprroved(PageRequest.of(offset - 1, size).withSort(Sort.by(field))).stream()
				.map(post -> postMapper.toPostApprovedDto(post)).collect(Collectors.toList());
		result.setPaginationPosts(listPostDto);
		result.setTotalPage(totalItem % size == 0 ? totalPage : totalPage + 1);
		return result;
	}

	@Override
	public PostDetailUserResponse getPostById(Long id) {

		PostDetailUserResponse postDetailUserResponse = new PostDetailUserResponse();

		Post post = postRepository.findById(id).get();
		PostDetailResponse postDetailResponse = postMapper.toDetailResponse(post);
		UserDto userDto = profileClient.getUserByUsername(post.getUsername());

		postDetailUserResponse.setPostDetailResponse(postDetailResponse);
		postDetailUserResponse.setUserDto(userDto);

		return postDetailUserResponse;

	}

	@Override
	public Long createPost(String token, PostDetailRequest postDetailRequest) {

		Post post = new Post();
		Category category = categoryRepository.findByCode(postDetailRequest.getCategoryCode());

		List<String> images = postDetailRequest.getImageUrls();

		StringBuilder slug = new StringBuilder(category.getCode());

		String strMaxDatetime = "9999-12-31 23:59:59.999999";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
		LocalDateTime maxDateTime = LocalDateTime.parse(strMaxDatetime, formatter);
		// get username
		String username = authClient.getUsername(token);

		post.setTitle(postDetailRequest.getTitle());
		post.setContent(postDetailRequest.getContent());
		post.setStatus(EStatus.PENDING);
		post.setPrice(postDetailRequest.getPrice());
		post.setSlug(slug.toString());
		post.setUsername(username);
		post.setDatePosted(maxDateTime);
		post.setDateCreated(LocalDateTime.now());
		post.setDateUpdated(LocalDateTime.now());
		post.setCategory(category);

		Post createdPost = postRepository.save(post);
		imageService.uploadImages(images, createdPost);

		return post.getId();
	}

	@Override
	public PostDetailResponse handlePost(Long id, EStatus status) {
		Post post = postRepository.findById(id).get();

		if (post == null)
			return null;

		post.setStatus(status);
		post.setDatePosted(LocalDateTime.now());
		postRepository.save(post);

		return postMapper.toDetailResponse(post);
	}

	@Override
	public PostDetailResponse updatePostById(Long id, PostDetailDto postDetailDto) {
		Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found by id"));
		List<Image> images = postDetailDto.getImageUrls().stream().map(url -> imageService.getImageByUrl(url))
				.collect(Collectors.toList());

		Category category = categoryRepository.findByCode(postDetailDto.getCategoryCode());

		post.setTitle(postDetailDto.getTitle());
		post.setContent(postDetailDto.getContent());
		post.setPrice(postDetailDto.getPrice());
		post.setUsername(postDetailDto.getUsername());
		post.setImages(images);
		post.setDateUpdated(LocalDateTime.now());
		post.setCategory(category);
		postRepository.save(post);
		return postMapper.toDetailResponse(post);
	}

	@Override
	public void deletePosts(List<Long> ids) {

		for (Long id : ids) {
			Post post = postRepository.findById(id).get();
			if (post != null)
				post.setStatus(EStatus.HIDDEN);
		}

	}

}
