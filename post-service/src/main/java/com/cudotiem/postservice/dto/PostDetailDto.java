package com.cudotiem.postservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.cudotiem.postservice.entity.Category;
import com.cudotiem.postservice.entity.Image;

import lombok.Data;

@Data
public class PostDetailDto {
	private String title;

	private String content;

	private Double price;

	private String url;

	private Long idUser;

	private List<String> imageNames;

	private LocalDateTime dateCreated;

	private LocalDateTime dateUpdated;

	private Long idCategory;
}
