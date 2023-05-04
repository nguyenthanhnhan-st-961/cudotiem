package com.cudotiem.postservice.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

	@Column(nullable = false, length = 100)
	private String title;

	private String content;
	
	@Enumerated(EnumType.STRING)
	private EStatus status;
	
	@Column(nullable = false)
	private Double price;
	
	@Column(nullable = false, unique = true)
	private String url;
	
	private Long idUser;
	
	@OneToMany(mappedBy = "post")
	private List<Image> images;
	
	@CreatedDate
//	@Column(nullable = false, updatable = false)
	private LocalDateTime dateCreated;
	
//	@Column(nullable = false)
	@LastModifiedDate
	private LocalDateTime dateUpdated;

	@ManyToOne
	@JoinColumn(name = "id_category")
	private Category category;

}
