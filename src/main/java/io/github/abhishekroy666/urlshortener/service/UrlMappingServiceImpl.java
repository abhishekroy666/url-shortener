package io.github.abhishekroy666.urlshortener.service;

import io.github.abhishekroy666.urlshortener.dto.UrlMappingDto;
import io.github.abhishekroy666.urlshortener.entity.UrlMapping;
import io.github.abhishekroy666.urlshortener.repository.UrlMappingRepository;
import io.github.abhishekroy666.urlshortener.util.Base62Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.NoSuchElementException;

/**
 * Service implementation that handles creation and retrieval of
 * URL mappings between the original long URL and the short token.
 * <p></p>
 * This class is responsible for persisting new mappings, encoding
 * database IDs into Base62 tokens, and building the user-facing
 * short URL. It also caches resolved mappings for faster lookups.
 */
@Service
@Slf4j
public class UrlMappingServiceImpl implements UrlMappingService {

    private final UrlMappingRepository repository;

    public UrlMappingServiceImpl(UrlMappingRepository repository) {
        this.repository = repository;
    }

    /**
     * Create or return an existing short-token mapping for the supplied long URL.
     * <p>
     * If the long URL already exists in the database, the existing mapping is used.
     * Otherwise, a new {@link io.github.abhishekroy666.urlshortener.entity.UrlMapping}
     * is persisted and its auto-generated ID is encoded to a Base62 token.
     * </p>
     *
     * @param longUrl the original long URL to shorten
     * @return a {@link UrlMappingDto} containing the original URL, token and short URL
     */
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

    /**
     * Resolve a short token back to its URL mapping.
     *
     * @param token Base62 short token
     * @return UrlMappingDto containing the long URL and related data
     * @throws NoSuchElementException if no mapping exists for the token
     */
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
