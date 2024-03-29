package com.cudotiem.postservice.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cudotiem.postservice.entity.EStatus;
import com.cudotiem.postservice.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	Post findBySlug(String slug);
	
	List<Post> findByPriceBetween(Double minPrice, Double maxPrice);
	
	@Query("select p from Post p where p.price between ?1 and ?2")
	List<Post> findByPricePagination(Double minPrice, Double maxPrice, Pageable pageable);
	
	
	@Query("select p from Post p where CONCAT(p.content, ' ', p.title) LIKE %?1%")
	List<Post> searchByKeyword(String keyword);
	
	@Query("select p from Post p where CONCAT(p.content, ' ', p.title) LIKE %?1%")
	List<Post> searchByKeyword(String keyword, Pageable pageable);

	@Query("select p from Post p where (:status is null or p.status = :status) and (:categoryCode is null or p.category.code = :categoryCode)")
	List<Post> findAllByStatus(EStatus status, String categoryCode);
	
	@Query("select p from Post p where (:status is null or p.status = :status) and (:categoryCode is null or p.category.code = :categoryCode)")

	List<Post> findAllByStatus(EStatus status, String categoryCode, Pageable pageable);
	
	@Query("select p from Post p where p.username like %:username% and (:status is null or p.status = :status)")
	List<Post> findAllByUsernameLikeAndStatus(String username, EStatus status);
	
	@Query("select p from Post p where p.username like %:username% and (:status is null or p.status = :status)")
	List<Post> findAllByUsernameLikeAndStatus(String username, EStatus status,Pageable pageable);
	
	List<Post> findByIdPost(Long idPost);
	

}
