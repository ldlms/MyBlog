package org.wildcodeschool.myblog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ImageDto;
import org.wildcodeschool.myblog.exception.RessourceNotFoundException;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ImageRepository;

@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}
	
	public List<ImageDto> getAllImages(){
		return imageRepository.findAll().stream().map(ImageDto::convertToDto).toList();
	}
	
	public ImageDto getImageById(Long id) {
		Image image =  imageRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("L'image avec l'id " + id + " n'a pas été trouvé"));
		return ImageDto.convertToDto(image);
	}
	
	public ImageDto createImage(Image image) {
		image.setCreatedAt(LocalDateTime.now());
		image.setUpdatedAt(LocalDateTime.now());
		imageRepository.save(image);
		return ImageDto.convertToDto(image);
		
	}
	
	public ImageDto updateImage(Long id, Image imageDetails) {
		Image imageToUpdate = imageRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("L'image avec l'id" + imageDetails.getId() + "n'a pas été trouvée"));
		imageToUpdate.setUpdatedAt(LocalDateTime.now());
		imageToUpdate.setUrl(imageDetails.getUrl());
		imageToUpdate.setArticles(imageDetails.getArticles());
		imageRepository.save(imageToUpdate);
		return ImageDto.convertToDto(imageToUpdate);
	}
	
	public boolean deleteImage(Long id) {
		Image imageToDelete = imageRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("L'image avec l'id" + id + "n'a pas été trouvé"));
		imageRepository.delete(imageToDelete);
		return true;
	}

}
