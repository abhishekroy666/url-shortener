package io.github.abhishekroy666.url_shortener.controller;

import io.github.abhishekroy666.url_shortener.dto.UrlMappingDto;
import io.github.abhishekroy666.url_shortener.service.UrlMappingService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UrlMappingController {

    private final UrlMappingService service;

    public UrlMappingController(UrlMappingService service) {
        this.service = service;
    }

    // Create Short URL
    @PostMapping("/shorten")
    public ResponseEntity<UrlMappingDto> shorten(@RequestBody UrlMappingDto dto) {
        String longUrl = dto.getLongUrl();
        if (longUrl == null || longUrl.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        log.info("longUrl: {}", longUrl);
        return ResponseEntity.ok(service.shortenUrl(longUrl));
    }

    // Expand Short URL token
    @GetMapping("/expand")
    public ResponseEntity<UrlMappingDto> expand(@RequestParam(name = "token") @NotEmpty(message = "Token cannot be empty") String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.getUrlMapping(token));
    }

    // Redirect Short URL -> Long URL
    @GetMapping("/{token}")
    public void redirect(@PathVariable String token, HttpServletResponse response) throws IOException {
        try {
            UrlMappingDto dto = service.getUrlMapping(token);
            log.info("Redirect to: {}", dto.getLongUrl());
            response.sendRedirect(dto.getLongUrl());
        } catch (IOException | NoSuchElementException e) {
            log.error("Exception occurred for token {}", token, e);
            response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}
