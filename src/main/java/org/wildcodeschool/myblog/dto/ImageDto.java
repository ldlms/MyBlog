package org.wildcodeschool.myblog.dto;

import java.util.List;

import org.wildcodeschool.myblog.model.Image;

public record ImageDto(Long id, String url, List<Long> articleIds) {
   public static ImageDto convertToDto(Image image) {
	   return new ImageDto(
			   image.getId(),
			   image.getUrl(),
			   image.getArticles() != null ? image.getArticles().stream().map(article -> article.getId()).toList():null
			   );
   };
    
	

    
}
