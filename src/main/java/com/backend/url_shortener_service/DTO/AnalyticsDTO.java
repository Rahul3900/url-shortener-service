package com.backend.url_shortener_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsDTO {

    private Long countClick = 0L;

    private LocalDateTime createdAt;

    private LocalDateTime lastUsed;
}
