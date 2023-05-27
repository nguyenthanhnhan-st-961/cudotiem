package com.cudotiem.postservice.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//đối tượng để hiển thị danh sách bài viết ở trang adimin
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostAdminDto extends PostDto {

	private String status;

	private Long dateCreated;

	private Long dateUpdated;

	private String username;

	private String category;
}
