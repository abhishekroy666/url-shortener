package io.github.abhishekroy666.url_shortener.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDto {

    private HttpStatus status;

    private String message;

}
