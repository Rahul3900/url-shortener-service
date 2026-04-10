package com.backend.url_shortener_service.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name="url_mapping",
        indexes = {
                @Index(name = "idx_short_code", columnList = "short_code"),
                @Index(name = "idx_created_at", columnList = "created_at")
        })
public class UrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_code",unique = true, nullable = false, length = 10)
    private String shortCode;

    @Column(name = "long_url", nullable = false,columnDefinition = "TEXT")
    private String longUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "click_count",nullable = false)
    private Long clickCount = 0L;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

    @Column(name = "custom_alias")
    private String customAlias = "False";

}
