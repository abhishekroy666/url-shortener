package io.github.abhishekroy666.url_shortener.service;

import io.github.abhishekroy666.url_shortener.dto.UrlMappingDto;
import io.github.abhishekroy666.url_shortener.entity.UrlMapping;
import io.github.abhishekroy666.url_shortener.repository.UrlMappingRepository;
import io.github.abhishekroy666.url_shortener.util.Base62Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class UrlMappingServiceImpl implements UrlMappingService {

    private final UrlMappingRepository repository;

    public UrlMappingServiceImpl(UrlMappingRepository repository) {
        this.repository = repository;
    }

    @Override
    public UrlMappingDto shortenUrl(String longUrl) {
        // Optional - Return existing token if URL already exists
        UrlMapping mapping = repository.findByLongUrl(longUrl)
                .orElseGet(() -> {
                    log.info("Unable to find longUrl: {} in DB. Persisting in DB", longUrl);
                    // Save URL to generate the Database Auto-Increment ID
                    UrlMapping newMapping = new UrlMapping();
                    newMapping.setLongUrl(longUrl);
                    newMapping = repository.save(newMapping);
                    // Encode the numeric ID to Base62 string token
                    return newMapping;
                });
        String token = Base62Encoder.encode(mapping.getId());
        String shortUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString() + "/api/v1/" + token;
        UrlMappingDto dto = new UrlMappingDto();
        dto.setLongUrl(mapping.getLongUrl());
        dto.setToken(token);
        dto.setShortUrl(shortUrl);
        return dto;
    }

    @Override
    @Cacheable(value = "urls", key = "#token")
    public UrlMappingDto getUrlMapping(String token) throws NoSuchElementException{
        // Decode the short string back to the database integer ID
        long id = Base62Encoder.decode(token);

        // Step 2: Query database directly via Primary Key (Highly Optimized)
        return repository.findById(id)
                .map(mapping -> {
                    String shortUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .build()
                            .toUriString() + "/api/v1/" + token;
                    UrlMappingDto dto = new UrlMappingDto();
                    dto.setLongUrl(mapping.getLongUrl());
                    dto.setToken(token);
                    dto.setShortUrl(shortUrl);
                    return dto;
                })
                .orElseThrow(() -> new NoSuchElementException("URL mapping not found for token: " + token));
    }
}
