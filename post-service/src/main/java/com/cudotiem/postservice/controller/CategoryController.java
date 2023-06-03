package com.cudotiem.postservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cudotiem.postservice.dto.CategoryDto;
import com.cudotiem.postservice.service.ICategoryService;

@RestController
@RequestMapping("/api/v1/post/category")
public class CategoryController {

	@Autowired
	ICategoryService categoryService;

	@Autowired
	MessageSource messageSource;

	@GetMapping()
	public ResponseEntity<List<CategoryDto>> getAllCategory(
			@RequestHeader(value = "Accept-Language", required = false) Locale locale) {
		List<CategoryDto> categoryDtos = new ArrayList<>();
		categoryService.getAllCategories().stream()
				.map(category -> categoryDtos.add(new CategoryDto(category.getId(), category.getCode(),
						category.getIcon(), messageSource.getMessage(category.getName(), null, locale))))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(categoryDtos);
	}
}
