package io.github.abhishekroy666.urlshortener.repository;

import io.github.abhishekroy666.urlshortener.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for CRUD operations on {@link UrlMapping} entities.
 * <p></p>
 * Extends Spring Data JPA's {@link JpaRepository} to provide common
 * persistence operations. Adds a convenience finder to look up a mapping
 * by the original long URL.
 */
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    /**
     * Find a UrlMapping by its original long URL.
     *
     * @param longUrl the original long URL
     * @return an Optional containing the mapping if present
     */
    Optional<UrlMapping> findByLongUrl(String longUrl);
}
