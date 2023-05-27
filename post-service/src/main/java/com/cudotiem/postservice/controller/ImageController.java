package com.cudotiem.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cudotiem.postservice.service.impl.ImageService;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

	@Autowired
	ImageService imageService;
	
}
