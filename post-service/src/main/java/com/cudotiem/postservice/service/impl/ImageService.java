package com.cudotiem.postservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cudotiem.postservice.dto.ImageDto;
import com.cudotiem.postservice.entity.Image;
import com.cudotiem.postservice.entity.Post;
import com.cudotiem.postservice.mapper.ImageMapper;
import com.cudotiem.postservice.repository.ImageRepository;
import com.cudotiem.postservice.service.IImageService;

@Service
public class ImageService implements IImageService {

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	ImageMapper mapper;

	@Override
	public Image getImageByUrl(String url) {
		return imageRepository.findByUrl(url);
	}


	@Override
	public List<Image> uploadImages(List<String> imagesUrls, Post post) {
		List<Image> images = new ArrayList<>();
		
		for(String url: imagesUrls) {
			Image image = new Image();
			image.setPost(post);
			image.setUrl(url);
			images.add(image);
		}
		imageRepository.saveAllAndFlush(images);
		return images;
	}

//	@Override
//	public String uploadImage(String path, MultipartFile imageFile) {
//
//		StringBuilder filePath = new StringBuilder(path);
//
//		File file = new File(filePath.toString());
//		if (!file.exists()) {
//			file.mkdir();
//		}
//
//		String name = imageFile.getOriginalFilename();
//		String randomID = UUID.randomUUID().toString();
//		String fileName = randomID.concat(name.substring(name.lastIndexOf(".")));
//
//		filePath.append(File.separator + fileName);
//
//		Image image = imageRepository.save(Image.builder()
//				.url(filePath.toString()).build());
//
//		try {
//			imageFile.transferTo(new File(filePath.toString()));
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		if (image != null) {
//			return "File upload successfully " + filePath.toString();
//		}
//
//		return "Failed upload file";
//	}
	
	@Override
	public ImageDto updateImage(Long id, ImageDto imageDto) {
		Image image = imageRepository.findById(id).get();
		if (image != null) {
			image.setUrl(imageDto.getUrl());

			return mapper.toImageDto(image);
		}
		return null;
	}

	@Override
	public void deleteImage(Long id) {
		imageRepository.deleteById(id);
	}

}
