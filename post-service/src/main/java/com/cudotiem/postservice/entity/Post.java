package com.cudotiem.postservice.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long idPost;

	@Column(nullable = false, length = 100)
	private String title;

	private String content;
	
	@Enumerated(EnumType.STRING)
	private EStatus status;
	
	@Column(nullable = false)
	private Double price;
	
	@Column(unique = true)
	private String slug;
	
	private String username;
	
	private LocalDateTime datePosted;
	
	@OneToMany(mappedBy = "post")
	private List<Image> images;
	
	@CreationTimestamp
	private LocalDateTime dateCreated;
	
	@UpdateTimestamp
	private LocalDateTime dateUpdated;

	@ManyToOne
	@JoinColumn(name = "id_category")
	private Category category;
	
}
