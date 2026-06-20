package io.github.abhishekroy666.urlshortener.controller;

import io.github.abhishekroy666.urlshortener.dto.UrlMappingDto;
import io.github.abhishekroy666.urlshortener.service.UrlMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that exposes API endpoints for creating and resolving
 * URL mappings.
 * <p></p>
 * Endpoints:
 * - POST  /api/v1/mapping/     : create a short URL for a supplied long URL
 * - GET   /api/v1/mapping/{token}: retrieve mapping details for a token
 */
@RestController
@RequestMapping("/api/v1/mapping")
@Slf4j
public class UrlMappingController {

    private final UrlMappingService service;

    public UrlMappingController(UrlMappingService service) {
        this.service = service;
    }

    /**
     * Create a short URL for the provided long URL.
     *
     * @param dto DTO containing the longUrl to shorten
     * @return 200 OK with UrlMappingDto on success or 400 Bad Request when input is invalid
     */
    @PostMapping("/")
    public ResponseEntity<UrlMappingDto> shorten(@RequestBody UrlMappingDto dto) {
        String longUrl = dto.getLongUrl();
        if (longUrl == null || longUrl.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        log.info("longUrl: {}", longUrl);
        return ResponseEntity.ok(service.shortenUrl(longUrl));
    }

    /**
     * Expand a short token to its mapping details.
     *
     * @param token Base62 token path variable
     * @return 200 OK with UrlMappingDto or 400 Bad Request if token is missing
     */
    @GetMapping("/{token}")
    public ResponseEntity<UrlMappingDto> expand(@PathVariable String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.getUrlMapping(token));
    }
}
