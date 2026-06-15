package io.github.abhishekroy666.url_shortener.controller;

import io.github.abhishekroy666.url_shortener.dto.UrlMappingDto;
import io.github.abhishekroy666.url_shortener.service.UrlMappingService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Controller responsible for redirecting short tokens to the original long URL.
 * <p>
 * Example: GET /api/v1/{token} will resolve the token and redirect the
 * client to the stored long URL. Returns a 404 if the token cannot be resolved.
 * </p>
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class RedirectController {

    private final UrlMappingService service;

    public RedirectController(UrlMappingService service) {
        this.service = service;
    }

    /**
     * Redirects a request for a short token to the corresponding long URL.
     *
     * @param token    the Base62 token path variable
     * @param response servlet response used to send the redirect or error
     * @throws IOException when sending the redirect fails
     */
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
