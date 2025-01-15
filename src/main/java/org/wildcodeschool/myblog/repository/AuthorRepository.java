package org.wildcodeschool.myblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.myblog.model.Author;

public interface AuthorRepository extends JpaRepository<Author,Long> {

	Optional <Author> findFirstByLastname(String lastname);
}
