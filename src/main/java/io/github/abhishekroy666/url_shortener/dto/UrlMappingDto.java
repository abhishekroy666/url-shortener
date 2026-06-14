package io.github.abhishekroy666.url_shortener.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UrlMappingDto {

    @NotEmpty(message = "Long URL cannot be empty")
    private String longUrl;

    private String shortUrl;

    private String token;
}
