package com.cudotiem.postservice.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PostDto {

	private String title;

	private Long price;

	private String url;

	private String imageUrl;

	private LocalDateTime dateCreated;
}
