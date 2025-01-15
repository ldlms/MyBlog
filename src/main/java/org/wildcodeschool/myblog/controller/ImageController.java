package org.wildcodeschool.myblog.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wildcodeschool.myblog.dto.ImageDto;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.ImageRepository;

@RestController
@RequestMapping("/images")
public class ImageController {

	private ImageRepository imageRepository;
	private ArticleRepository articleRepository;
	
	public ImageController(ImageRepository imageRepository, ArticleRepository articleRepository ) {
		this.imageRepository = imageRepository;
		this.articleRepository = articleRepository;
	}
	
	@GetMapping
	public ResponseEntity<List<ImageDto>> getAllImages(){
		List<ImageDto> imageDto	= imageRepository.findAll()
								.stream()
								.map(this::convertToDTO)
								.toList();
		
		return imageDto.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(imageDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ImageDto> getImageById(@PathVariable Long id){
		Image image = imageRepository.findById(id).orElse(null);
		
		return image == null? ResponseEntity.notFound().build():ResponseEntity.ok(convertToDTO(image));
	}
	
	@PostMapping
	public ResponseEntity<ImageDto> createImage(@RequestBody Image image){
		image.setCreatedAt(LocalDateTime.now());
		image.setUpdatedAt(LocalDateTime.now());
		
		Image imageToSave = imageRepository.save(image);
		return ResponseEntity.status(201).body(convertToDTO(imageToSave));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ImageDto> updateImage(@RequestBody Image imageDetails, @PathVariable Long id){
		Image image = imageRepository.findById(id).orElse(null);
		if(image == null) {
			return ResponseEntity.notFound().build();
		}
		image.setUpdatedAt(LocalDateTime.now());
		image.setUrl(imageDetails.getUrl());
		image.setArticles(imageDetails.getArticles());
		
		imageRepository.save(image);
		return ResponseEntity.ok(convertToDTO(image));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteImage(@PathVariable Long id){
		Image image = imageRepository.findById(id).orElse(null);
		if(image == null) {
			return ResponseEntity.notFound().build();
		}
		
		imageRepository.delete(image);
		return ResponseEntity.noContent().build();
	}
	
	private ImageDto convertToDTO(Image image) {
		ImageDto imageDto = new ImageDto();
		
		imageDto.setId(image.getId());
		imageDto.setUrl(image.getUrl());
		if(image.getArticles() != null) {
			imageDto.setArticleIds(image.getArticles().stream().map(articles -> articles.getId()).collect(Collectors.toList()));
		}
		
		return imageDto;
	}
	
}
