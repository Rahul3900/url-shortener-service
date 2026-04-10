package com.backend.url_shortener_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UrlResponseDTO {

    private String shortCode;

    private String shortUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

}
