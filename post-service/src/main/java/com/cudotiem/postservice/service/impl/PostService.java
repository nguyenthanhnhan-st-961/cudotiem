package com.cudotiem.postservice.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cudotiem.postservice.dto.UserDto;
import com.cudotiem.postservice.dto.post.PostAdminDto;
import com.cudotiem.postservice.dto.post.PostApprovedDto;
import com.cudotiem.postservice.dto.post.PostDto;
import com.cudotiem.postservice.dto.post.PostUserDto;
import com.cudotiem.postservice.entity.Category;
import com.cudotiem.postservice.entity.EStatus;
import com.cudotiem.postservice.entity.Image;
import com.cudotiem.postservice.entity.Post;
import com.cudotiem.postservice.exception.TokenExpiredException;
import com.cudotiem.postservice.mapper.ImageMapper;
import com.cudotiem.postservice.mapper.PostMapper;
import com.cudotiem.postservice.payload.request.PostDetailRequest;
import com.cudotiem.postservice.payload.response.PostDetailResponse;
import com.cudotiem.postservice.payload.response.PostDetailUserResponse;
import com.cudotiem.postservice.payload.response.PostPaginationResponse;
import com.cudotiem.postservice.repository.CategoryRepository;
import com.cudotiem.postservice.repository.ImageRepository;
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
	ImageRepository imageRepository;

	@Autowired
	PostMapper postMapper;

	@Autowired
	ImageMapper imageMapper;

	@Autowired
	ProfileClient profileClient;

	@Autowired
	AuthClient authClient;

	@Autowired
	MessageSource messageSource;

	@Override
	public List<PostDto> getAllPosts() {
		return postRepository.findAll().stream().map(post -> postMapper.toPostDto(post)).collect(Collectors.toList());

	}

	@Override
	public PostPaginationResponse<PostAdminDto> getPostsAdmin(Locale locale, EStatus status, int offset, int size,
			String field) {
		PostPaginationResponse<PostAdminDto> result = new PostPaginationResponse<>();
		Integer totalItem = postRepository.findAllByStatus(status, null).size();
		Integer totalPage = totalItem / size;

		List<PostAdminDto> listPostDto = postRepository
				.findAllByStatus(status, null,
						PageRequest.of(offset - 1, size).withSort(Sort.by(Sort.Direction.DESC, field)))
				.stream().map(post -> {
					PostAdminDto postAdminDto = postMapper.toPostAdminDto(post);
					postAdminDto.setCategoryName(messageSource.getMessage(post.getCategory().getName(), null, locale));
					return postAdminDto;
				}).collect(Collectors.toList());
		result.setPaginationPosts(listPostDto);
		result.setTotalPage(totalItem % size == 0 ? totalPage : totalPage + 1);
		return result;
	}

	@Override
	public PostPaginationResponse<PostUserDto> getPostsByUsername(String token, EStatus status, int offset, int size,
			String field) {
		PostPaginationResponse<PostUserDto> result = new PostPaginationResponse<>();
		// get username
		String username = null;
		try {
			username = authClient.getUsername(token);
		} catch (Exception e) {
			throw new TokenExpiredException("access token is expired");
		}

		Integer totalItem = postRepository.findAllByUsernameLikeAndStatus(username, status).size();
		Integer totalPage = totalItem / size;
		List<PostUserDto> listPostDto = postRepository
				.findAllByUsernameLikeAndStatus(username, status,
						PageRequest.of(offset - 1, size).withSort(Sort.by(Sort.Direction.DESC, field)))
				.stream().map(post -> postMapper.toPostUserDto(post)).collect(Collectors.toList());
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
	public PostPaginationResponse<PostApprovedDto> filterPostsByPriceAndPagination(double min, double max, int offset,
			int size, String field) {
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

//	@Override
//	public PostPaginationResponse<PostUserDto> getPostsByStatus(EStatus status, int offset, int size, String field) {
//		PostPaginationResponse<PostUserDto> result = new PostPaginationResponse<>();
//		Integer totalItem = postRepository.findAllByStatus(status).size();
//		Integer totalPage = totalItem / size;
//		List<PostUserDto> listPostDto = postRepository
//				.findAllByStatus(status, PageRequest.of(offset - 1, size).withSort(Sort.by(field))).stream()
//				.map(post -> postMapper.toPostUserDto(post)).collect(Collectors.toList());
//		result.setPaginationPosts(listPostDto);
//		result.setTotalPage(totalItem % size == 0 ? totalPage : totalPage + 1);
//		return result;
//	}

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
	public PostPaginationResponse<PostApprovedDto> getPostsApproved(String categoryCode, int offset, int size,
			String field) {

		PostPaginationResponse<PostApprovedDto> result = new PostPaginationResponse<>();
		Integer totalItem = postRepository.findAllByStatus(EStatus.APPROVED, categoryCode).size();
		Integer totalPage = totalItem / size;
		List<PostApprovedDto> listPostDto = postRepository
				.findAllByStatus(EStatus.APPROVED, categoryCode,
						PageRequest.of(offset - 1, size).withSort(Sort.by(field)))
				.stream().map(post -> postMapper.toPostApprovedDto(post)).collect(Collectors.toList());
		result.setPaginationPosts(listPostDto);
		result.setTotalPage(totalItem % size == 0 ? totalPage : totalPage + 1);
		return result;
	}

	@Override
	public PostDetailUserResponse getPostById(Long id, Locale locale) {

		PostDetailUserResponse postDetailUserResponse = new PostDetailUserResponse();

		Post post = postRepository.findById(id).get();
		PostDetailResponse postDetailResponse = postMapper.toDetailResponse(post);
		postDetailResponse.setCategoryName(messageSource.getMessage(post.getCategory().getName(), null, locale));
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

		post.setIdPost(-1L);
		post.setTitle(postDetailRequest.getTitle());
		post.setContent(postDetailRequest.getContent());
		post.setStatus(EStatus.CREATE_PENDING);
		post.setPrice(postDetailRequest.getPrice());
		post.setSlug(slug.toString());
		post.setUsername(username);
		post.setDatePosted(maxDateTime);
		post.setDateCreated(LocalDateTime.now());
		post.setDateUpdated(LocalDateTime.now());
		post.setCategory(category);

		Post createdPost = postRepository.save(post);
		post.setIdPost(post.getId());
		imageService.uploadImages(images, createdPost);

		postRepository.save(createdPost);

		return post.getIdPost();
	}

	@Override
	public PostDetailResponse handlePost(Long id, EStatus status) {
		Post post = postRepository.findById(id).get();

		if (post == null)
			return null;

		if (status.equals(EStatus.APPROVED)) {
			post.setStatus(EStatus.APPROVED);
			post.setDatePosted(LocalDateTime.now());
		} else if (status.equals(EStatus.CREATE_REJECTED)) {
			post.setStatus(EStatus.CREATE_REJECTED);
		} else {
			post.setStatus(EStatus.HIDDEN);
			post.setDateUpdated(LocalDateTime.now());
		}
		postRepository.save(post);
		return postMapper.toDetailResponse(post);
	}

	@Override
	
	public PostDetailResponse updateApproved(Long id, EStatus status) {
		List<Post> posts = postRepository.findByIdPost(id);
		if (posts == null || posts.size() != 2)
			return null;
		Post oldPost = posts.get(0).getDateCreated().isBefore(posts.get(1).getDateCreated()) ? posts.get(0)
				: posts.get(1);
		Post newPost = posts.get(0).getDateCreated().isBefore(posts.get(1).getDateCreated()) ? posts.get(1)
				: posts.get(0);

		if (status.equals(EStatus.APPROVED)) {
			oldPost.setTitle(newPost.getTitle());
			oldPost.setContent(newPost.getContent());
			oldPost.setPrice(newPost.getPrice());
//			oldPost.setImages(newPost.getImages());
			oldPost.setCategory(newPost.getCategory());
//			imageRepository.deleteAllByPost(newPost);
//			oldPost.setStatus(status);
		} else {
			oldPost.setStatus(EStatus.APPROVED);
		}

		oldPost.setDateUpdated(LocalDateTime.now());
		
		postRepository.delete(newPost);
		postRepository.save(oldPost);

		return postMapper.toDetailResponse(oldPost);
	}

	@Override
	public String updatePostById(Long id, String token, EStatus status, PostDetailRequest postDetailRequest) {

		if (postDetailRequest != null) {

			Post newContentPost = new Post();
			List<String> images = postDetailRequest.getImageUrls();

			Category category = categoryRepository.findByCode(postDetailRequest.getCategoryCode());
			StringBuilder slug = new StringBuilder(category.getCode());

			String strMaxDatetime = "9999-12-31 23:59:59.999999";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
			LocalDateTime maxDateTime = LocalDateTime.parse(strMaxDatetime, formatter);
			String username = authClient.getUsername(token);

			newContentPost.setIdPost(id);
			newContentPost.setTitle(postDetailRequest.getTitle());
			newContentPost.setContent(postDetailRequest.getContent());
			newContentPost.setStatus(EStatus.UPDATE_PENDING);
			newContentPost.setPrice(postDetailRequest.getPrice());
			newContentPost.setSlug(slug.toString());
			newContentPost.setUsername(username);
			newContentPost.setDatePosted(maxDateTime);
			newContentPost.setDateUpdated(LocalDateTime.now());
			newContentPost.setCategory(category);

			Post updatedPost = postRepository.save(newContentPost);
			imageService.uploadImages(images, updatedPost);

			return updatedPost instanceof Post ? "the post has been sent" : null;
		} else {
			Post oldContentPost = postRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Post not found by id"));
			oldContentPost.setStatus(status);
			return postRepository.save(oldContentPost) instanceof Post ? "the post has been sent" : null;
		}

	}

	@Override
	public void deletePosts(List<Long> ids) {

		for (Long id : ids) {
			Post post = postRepository.findById(id).get();
			if (post != null)
				post.setStatus(EStatus.HIDDEN);
			post.setDateUpdated(LocalDateTime.now());
		}

	}

	@Override
	public List<EStatus> getAllStatus() {
		return Arrays.asList(EStatus.values());
	}

}
