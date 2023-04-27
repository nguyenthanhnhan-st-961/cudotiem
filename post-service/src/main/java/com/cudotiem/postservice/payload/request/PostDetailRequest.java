package com.cudotiem.postservice.payload.request;

import java.util.List;

import lombok.Data;

@Data
public class PostDetailRequest {

	private String title;

	private String content;

	private Double price;

	private String url;

	private Long idUser;

	private List<String> imageUrls;

	private Long idCategory;
}
