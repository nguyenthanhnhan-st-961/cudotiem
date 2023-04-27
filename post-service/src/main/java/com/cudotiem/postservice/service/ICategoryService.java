package com.cudotiem.postservice.service;

import java.util.List;
import java.util.Optional;

import com.cudotiem.postservice.entity.Category;

public interface ICategoryService {

	List<Category> getAllCategories();
	Category getCategoryById(Long id);
	Category createCategory(Category category);
	Category updateCategoryById(Long id, Category category);
	void deleteCategories(List<Category> categories);
}
