package com.backend.url_shortener_service.Service;

import com.backend.url_shortener_service.DTO.AnalyticsDTO;
import com.backend.url_shortener_service.DTO.UrlResponseDTO;
import com.backend.url_shortener_service.DTO.UrlUserDTO;

public interface UrlService {

    public UrlResponseDTO saveUrl(UrlUserDTO urlUserDTO);

    public String getUrlFromCode(String code);

    public void checkCount(String ip);

    public AnalyticsDTO getAnalytics(String code);

    public String getUrlInfoFromCode(String code);
}
