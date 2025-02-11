package org.wildcodeschool.myblog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.CreateArticleDto;
import org.wildcodeschool.myblog.dto.CreateImageDto;
import org.wildcodeschool.myblog.dto.ImageDto;
import org.wildcodeschool.myblog.exception.RessourceNotFoundException;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.ImageRepository;

@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	private final ArticleRepository articleRepository;
	
	public ImageService(ImageRepository imageRepository, ArticleRepository articleRepository) {
		this.imageRepository = imageRepository;
		this.articleRepository = articleRepository;
	}
	
	public List<ImageDto> getAllImages(){
		return imageRepository.findAll().stream().map(ImageDto::convertToDto).toList();
	}
	
	public ImageDto getImageById(Long id) {
		Image image =  imageRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("L'image avec l'id " + id + " n'a pas été trouvé"));
		return ImageDto.convertToDto(image);
	}
	
	public ImageDto createImage(CreateImageDto imageDto) {
		Image image = CreateImageDto.convertToEntity(imageDto);
		imageRepository.save(image);
		return ImageDto.convertToDto(image);
		
	}
	
	public ImageDto updateImage(Long id, CreateImageDto imageDetails) {
		Image imageToUpdate = imageRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("L'image avec l'id" + id + "n'a pas été trouvée"));
		imageToUpdate.setUpdatedAt(LocalDateTime.now());
		imageToUpdate.setUrl(imageDetails.url());
		if(imageDetails.articles() != null) {
			List<Article> articlesList = new ArrayList<Article>();
			for(CreateArticleDto articleDto : imageDetails.articles()) {
				Article article = articleRepository.findByTitle(articleDto.title())
						.orElseThrow(() -> new RessourceNotFoundException("L'article avec l'id" + articleDto.title() + "n'a pas été trouvé"));
				articlesList.add(article);
			}
			imageToUpdate.setArticles(articlesList);
		}
		
		imageRepository.save(imageToUpdate);
		return ImageDto.convertToDto(imageToUpdate);
	}
	
	public boolean deleteImage(Long id) {
		Image imageToDelete = imageRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("L'image avec l'id" + id + "n'a pas été trouvé"));
		imageRepository.delete(imageToDelete);
		return true;
	}

}
