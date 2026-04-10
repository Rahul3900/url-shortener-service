package com.backend.url_shortener_service.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlUserDTO {

    @NotBlank(message = "URL cannot be null")
    @URL(message = "Invalid URL format")
    private String longUrl;

    private String customAlias;
}
