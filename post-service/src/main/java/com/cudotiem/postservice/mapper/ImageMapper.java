package com.cudotiem.postservice.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.cudotiem.postservice.dto.ImageDto;
import com.cudotiem.postservice.entity.Image;

@Configuration
public class ImageMapper {

	@Autowired
	ModelMapper mapper;
	
	public Image toImage(ImageDto imageDto) {
		return mapper.map(imageDto, Image.class);
	}
	
	public ImageDto toImageDto(Image image) {
		return mapper.map(image, ImageDto.class);
	}
}
