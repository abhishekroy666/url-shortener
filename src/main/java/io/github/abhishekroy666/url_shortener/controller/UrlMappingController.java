package io.github.abhishekroy666.url_shortener.controller;

import io.github.abhishekroy666.url_shortener.dto.UrlMappingDto;
import io.github.abhishekroy666.url_shortener.service.UrlMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mapping")
@Slf4j
public class UrlMappingController {

    private final UrlMappingService service;

    public UrlMappingController(UrlMappingService service) {
        this.service = service;
    }

    // Create Short URL
    @PostMapping("/")
    public ResponseEntity<UrlMappingDto> shorten(@RequestBody UrlMappingDto dto) {
        String longUrl = dto.getLongUrl();
        if (longUrl == null || longUrl.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        log.info("longUrl: {}", longUrl);
        return ResponseEntity.ok(service.shortenUrl(longUrl));
    }

    // Expand Short URL token
    @GetMapping("/{token}")
    public ResponseEntity<UrlMappingDto> expand(@PathVariable(name = "token") String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.getUrlMapping(token));
    }
}
