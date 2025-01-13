package org.wildcodeschool.myblog.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.myblog.model.Article;

public interface ArticleRepository extends JpaRepository<Article,Long> {

	List<Article> findByArticleContaining(String characters);
	
	List<Article> findByCreatedAtAfter(Date creationDate);
	
	List<Article> findTop5ByOrderByCreatedAtDesc();
}
