package com.cudotiem.postservice.dto.post;

import java.util.List;

import lombok.Data;

// đối tượng hiển thị chi tiết bài viết ở trang admin
@Data
public class PostDetailDto {
	private Long id;
	
	private String title;

	private Double price;

	private String slug;

	private List<String> imageUrls;

	private Long postedDate;
	
	private Long createdDate;
	
	private Long updatedDate;
	
	private String username;
	
	private String status;
	
	private String categoryCode;
	
	private String content;

}
