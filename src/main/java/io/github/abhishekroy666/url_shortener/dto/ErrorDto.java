package io.github.abhishekroy666.url_shortener.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * DTO representing an error response returned by the API.
 * Contains an HTTP status and an optional human-readable message.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDto {

    /** HTTP status that describes the error (e.g. 404 NOT_FOUND). */
    private HttpStatus status;

    /** Optional error message with additional context. */
    private String message;

}
