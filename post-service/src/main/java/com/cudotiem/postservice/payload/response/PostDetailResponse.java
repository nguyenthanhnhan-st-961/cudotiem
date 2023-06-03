package com.cudotiem.postservice.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// đối tượng trả về chi tiết bài viết hiển thị cho người dùng
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailResponse {

	private Long id;
	
	private String title;

	private String content;

	private Double price;

	private String slug;

	private String username;

	private List<String> imageUrls;

	private Long datePosted;

	private String categoryName;
}
