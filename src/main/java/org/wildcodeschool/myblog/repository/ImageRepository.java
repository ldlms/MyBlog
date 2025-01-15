package org.wildcodeschool.myblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.myblog.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
	
	Optional<Image> findByUrl(String url);
}
