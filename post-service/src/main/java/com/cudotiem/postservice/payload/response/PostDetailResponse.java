package com.cudotiem.postservice.payload.response;

import java.time.LocalDateTime;
import java.util.List;

import com.cudotiem.postservice.entity.Category;

import lombok.Data;

@Data
public class PostDetailResponse {

	private Long id;
	
	private String title;

	private String content;

	private Double price;

	private String url;

	private Long idUser;

	private List<String> imageUrls;

	private LocalDateTime dateCreated;

	private LocalDateTime dateUpdated;

	private Category Category;
}
