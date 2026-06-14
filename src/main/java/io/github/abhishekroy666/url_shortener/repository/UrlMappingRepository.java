package io.github.abhishekroy666.url_shortener.repository;

import io.github.abhishekroy666.url_shortener.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByLongUrl(String longUrl);
}
