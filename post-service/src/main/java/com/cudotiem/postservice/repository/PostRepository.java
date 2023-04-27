package com.cudotiem.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cudotiem.postservice.dto.PostDetailDto;
import com.cudotiem.postservice.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	Post findByUrl(String url);

}
