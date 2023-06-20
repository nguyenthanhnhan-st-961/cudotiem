package com.cudotiem.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.cudotiem.postservice.entity.Image;
import com.cudotiem.postservice.entity.Post;

import jakarta.transaction.Transactional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	Image findByUrl(String url);
	
	@Transactional
	@Modifying
	void deleteAllByPost(Post post);
}
