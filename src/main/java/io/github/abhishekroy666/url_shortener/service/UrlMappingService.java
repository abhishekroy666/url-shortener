package io.github.abhishekroy666.url_shortener.service;

import io.github.abhishekroy666.url_shortener.dto.UrlMappingDto;

import java.util.NoSuchElementException;

public interface UrlMappingService {

    UrlMappingDto shortenUrl(String longUrl);

    UrlMappingDto getUrlMapping(String token) throws NoSuchElementException;
}
