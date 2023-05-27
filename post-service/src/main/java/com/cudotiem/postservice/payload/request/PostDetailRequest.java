package com.cudotiem.postservice.payload.request;

import java.util.List;

import lombok.Data;

// đối tượng người dùng gửi yêu cầu thêm và chỉnh sửa bài viết
@Data
public class PostDetailRequest {

	private String title;

	private String content;

	private Double price;

	private List<String> imageUrls;

	private String categoryCode;
}
