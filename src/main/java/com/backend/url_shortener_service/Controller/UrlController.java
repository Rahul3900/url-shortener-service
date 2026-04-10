package com.backend.url_shortener_service.Controller;

import com.backend.url_shortener_service.DTO.AnalyticsDTO;
import com.backend.url_shortener_service.DTO.UrlResponseDTO;
import com.backend.url_shortener_service.DTO.UrlUserDTO;
import com.backend.url_shortener_service.Service.UrlService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/url_service")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponseDTO> saveUrl(@Valid @RequestBody UrlUserDTO urlUserDTO, HttpServletRequest httpServletRequest){

        String ip = httpServletRequest.getRemoteAddr();

        urlService.checkCount(ip);
        UrlResponseDTO urlResponseDTO1 = urlService.saveUrl(urlUserDTO);

        return new ResponseEntity<>(urlResponseDTO1,HttpStatus.CREATED);
    }

    @Hidden
    @GetMapping("{code}")
    public ResponseEntity<Void> getLongUrl(@PathVariable String code){

        String longUrl = urlService.getUrlFromCode(code);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl)).build();
    }

    @GetMapping("info/{code}")
    public ResponseEntity<String> getLongUrlInfo(@PathVariable String code){

        String longUrl = urlService.getUrlInfoFromCode(code);

        return ResponseEntity.ok(longUrl);
    }

    @GetMapping("getAnalytics/{code}")
    public ResponseEntity<AnalyticsDTO> getClickCount(@PathVariable String code){

        AnalyticsDTO analyticsDTO = urlService.getAnalytics(code);

        return new ResponseEntity<>(analyticsDTO,HttpStatus.ACCEPTED);
    }
}
