package io.github.abhishekroy666.urlshortener.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entity representing a mapping from a long URL to a database ID.
 * <p>
 * The ID is auto-generated and used to produce a compact Base62 token
 * for the short URL. The entity also stores the creation timestamp.
 * </p>
 */
@Entity
@Table(name = "url_mapping")
@Data
public class UrlMapping {

    /** Primary key used to generate the short token. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The original long URL provided by the user. */
    @Column(nullable = false, length = 2048)
    private String longUrl;

    /**
     * Record creation timestamp. Defaults to now and is not updatable.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
