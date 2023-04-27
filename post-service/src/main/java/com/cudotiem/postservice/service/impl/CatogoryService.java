package com.cudotiem.postservice.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cudotiem.postservice.entity.Category;
import com.cudotiem.postservice.repository.CategoryRepository;
import com.cudotiem.postservice.service.ICategoryService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CatogoryService implements ICategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category getCategoryById(Long id) {
		Category category = categoryRepository.findById(id).get();
		if(category != null) {
			return category;
		}
		return null;
	}

	@Override
	public Category createCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public Category updateCategoryById(Long id, Category categoryRequest) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
		category.setName(categoryRequest.getName());
		category.setPosts(categoryRequest.getPosts());
		return categoryRepository.save(category);
	}

	@Override
	public void deleteCategories(List<Category> categories) {
		for (Category category : categories) {
			if (categoryRepository.findById(category.getId()) != null) {
				categoryRepository.delete(category);
			}
		}
	}

}
