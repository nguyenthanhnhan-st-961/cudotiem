package com.cudotiem.postservice.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cudotiem.postservice.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	Post findByUrl(String url);
	
	List<Post> findByPriceBetween(Double minPrice, Double maxPrice);
	
	@Query("select p from Post p where p.price between ?1 and ?2")
	List<Post> findByPricePagination(Double minPrice, Double maxPrice, Pageable pageable);
	
	
	List<Post> findAllByTitleLike(String title);
	
	@Query("select p from Post p where p.title like %?1%")
	List<Post> findAllByTitle(String title, Pageable pageable);

}
