package io.github.abhishekroy666.url_shortener.exception;

import io.github.abhishekroy666.url_shortener.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(HttpStatus.NOT_FOUND);
        errorDto.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }
}
