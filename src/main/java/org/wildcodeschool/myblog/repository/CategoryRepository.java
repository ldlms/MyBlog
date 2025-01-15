package org.wildcodeschool.myblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.model.Image;


public interface CategoryRepository extends JpaRepository<Category,Long> {
	
	Optional<Category> findByName(String url);

}
