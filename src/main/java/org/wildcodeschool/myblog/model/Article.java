package org.wildcodeschool.myblog.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	@ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
	
	@ManyToMany
	@JoinTable(
	name = "article_image",
	joinColumns = @JoinColumn(name="article_id"),
	inverseJoinColumns = @JoinColumn(name="image_id")
    )
	private List<Image> images;
	
	@OneToMany(mappedBy = "article")
    private List<ArticleAuthor> articleAuthors;
	
	public Article() {}
	
	public Article(String title, String content) {
		this.title = title;
		this.content = content;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<ArticleAuthor> getArticleAuthors() {
		return articleAuthors;
	}

	public void setArticleAuthors(List<ArticleAuthor> articleAuthors) {
		this.articleAuthors = articleAuthors;
	}
	
}
