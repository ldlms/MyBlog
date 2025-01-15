package org.wildcodeschool.myblog.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private String categoryName;
    private List<Long> imagesId; 

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


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

	public List<Long> getImagesId() {
		return imagesId;
	}

	public void setImagesId(List<Long> imagesId) {
		this.imagesId = imagesId;
	}
    
    
}
