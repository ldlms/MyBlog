package org.wildcodeschool.myblog.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.myblog.model.Article;

public interface ArticleRepository extends JpaRepository<Article,Long> {

	List<Article> findBycontentContaining(String search);
	
	List<Article> findByCreatedAtAfter(LocalDateTime creationDate);
	
	List<Article> findTop5ByOrderByCreatedAtDesc();
}
