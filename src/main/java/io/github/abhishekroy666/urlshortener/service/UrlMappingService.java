package io.github.abhishekroy666.urlshortener.service;

import io.github.abhishekroy666.urlshortener.dto.UrlMappingDto;

import java.util.NoSuchElementException;

/**
 * Service API for creating and resolving URL mappings.
 */
public interface UrlMappingService {

    /**
     * Create or return a short mapping for the supplied long URL.
     *
     * @param longUrl the original URL to shorten
     * @return UrlMappingDto containing longUrl, token and shortUrl
     */
    UrlMappingDto shortenUrl(String longUrl);

    /**
     * Retrieve the URL mapping corresponding to the given short token.
     *
     * @param token Base62 token representing the mapping
     * @return UrlMappingDto with mapping details
     * @throws NoSuchElementException if mapping is not found
     */
    UrlMappingDto getUrlMapping(String token) throws NoSuchElementException;
}
