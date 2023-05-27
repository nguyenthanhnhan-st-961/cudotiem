package com.cudotiem.postservice.payload.response;

import java.util.List;

import com.cudotiem.postservice.dto.post.PostDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// đối tượng trả về danh sách bài viết cùng với totalPage dùng để hiển thị phân trang
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPaginationResponse<T extends PostDto> {

	private List<T> paginationPosts;
	private Integer totalPage;
}
