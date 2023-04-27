package com.cudotiem.postservice.service;

import java.util.List;

import com.cudotiem.postservice.dto.ImageDto;
import com.cudotiem.postservice.entity.Image;
import com.cudotiem.postservice.entity.Post;

public interface IImageService {

	Image getImageByUrl(String url);

	List<Image> uploadImages(List<String> imagesUrls, Post post);
	
	ImageDto updateImage(Long id, ImageDto imageDto);

	void deleteImage(Long id);
	
//	void setPost(List<Image> images, Post post);
}
