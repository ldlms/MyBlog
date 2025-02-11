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
import org.wildcodeschool.myblog.dto.CreateImageDto;
import org.wildcodeschool.myblog.dto.ImageDto;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.ImageRepository;
import org.wildcodeschool.myblog.service.ImageService;

@RestController
@RequestMapping("/images")
public class ImageController {

	private ImageService imageService;

	
	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}
	
	@GetMapping
	public ResponseEntity<List<ImageDto>> getAllImages(){
		List<ImageDto> images = imageService.getAllImages();
		
		return images.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(images);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ImageDto> getImageById(@PathVariable Long id){
		ImageDto image = imageService.getImageById(id);
		
		return image == null? ResponseEntity.notFound().build():ResponseEntity.ok(image);
	}
	
	@PostMapping
	public ResponseEntity<ImageDto> createImage(@RequestBody CreateImageDto image){
		return ResponseEntity.ok(imageService.createImage(image));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ImageDto> updateImage(@RequestBody CreateImageDto imageDetails, @PathVariable Long id){
		return ResponseEntity.ok(imageService.updateImage(id, imageDetails));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteImage(@PathVariable Long id){
		imageService.deleteImage(id);
		return ResponseEntity.noContent().build();
	}
	
}
