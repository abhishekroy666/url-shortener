package io.github.abhishekroy666.url_shortener.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UrlMappingDto {

    @NotEmpty(message = "Long URL cannot be empty")
    private String longUrl;

    private String shortUrl;

    private String token;
}
