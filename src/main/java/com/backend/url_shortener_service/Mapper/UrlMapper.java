package com.backend.url_shortener_service.Mapper;

import com.backend.url_shortener_service.DTO.AnalyticsDTO;
import com.backend.url_shortener_service.DTO.UrlResponseDTO;
import com.backend.url_shortener_service.DTO.UrlUserDTO;
import com.backend.url_shortener_service.Entity.UrlEntity;
import org.springframework.stereotype.Component;

@Component
public class UrlMapper {

    private static final String url = "http://localhost:8080/url_service/";

    public UrlResponseDTO mapToUrlDTO(UrlEntity urlEntity){
        return new UrlResponseDTO(
                urlEntity.getShortCode(),
                url+urlEntity.getShortCode(),
                urlEntity.getCreatedAt(),
                urlEntity.getExpiredAt()
        );
    }

    public UrlEntity mapToUrlEntity(UrlUserDTO urlUserDTO){
        UrlEntity urlEntity = new UrlEntity();

        urlEntity.setLongUrl(urlUserDTO.getLongUrl());

        return urlEntity;
    }

    public AnalyticsDTO mapToAnalytics(UrlEntity urlEntity){

        AnalyticsDTO analyticsDTO = new AnalyticsDTO();
        analyticsDTO.setCreatedAt(urlEntity.getCreatedAt());

        return analyticsDTO;
    }

}
