package io.github.abhishekroy666.urlshortener.exception;

import io.github.abhishekroy666.urlshortener.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

/**
 * Global exception handler that maps common exceptions to HTTP responses.
 * <p>
 * Currently handles {@link NoSuchElementException} by returning a 404
 * response with an {@link ErrorDto} body. Additional handlers can be
 * added here to centralize error handling for the API.
 * </p>
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle resource not found exceptions.
     *
     * @param exception the thrown NoSuchElementException
     * @return ResponseEntity containing an {@link ErrorDto} and 404 status
     */
    @ExceptionHandler(exception = NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(HttpStatus.NOT_FOUND);
        errorDto.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }
}
