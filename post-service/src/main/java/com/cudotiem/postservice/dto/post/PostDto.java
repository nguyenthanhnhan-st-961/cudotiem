package com.cudotiem.postservice.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// đối tượng để hiển thị danh sách bài viết ở trang user
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

	private Long id;
	
	private String title;

	private Double price;

	private String slug;

	private String thumbnail;

	private Long datePosted;
}
