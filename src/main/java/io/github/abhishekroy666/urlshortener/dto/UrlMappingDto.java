package io.github.abhishekroy666.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Data transfer object used for creating and returning URL mappings.
 * <p>
 * Fields:
 * - longUrl: the original URL supplied by the user
 * - shortUrl: the fully-qualified short URL returned to the user
 * - token: the Base62 token representing the mapping
 * </p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UrlMappingDto {

    @NotEmpty(message = "Long URL cannot be empty")
    private String longUrl;

    private String shortUrl;

    private String token;
}
