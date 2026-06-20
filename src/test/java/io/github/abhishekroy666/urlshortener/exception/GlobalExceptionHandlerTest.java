package io.github.abhishekroy666.urlshortener.exception;

import io.github.abhishekroy666.urlshortener.dto.ErrorDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GlobalExceptionHandler.
 * Tests the global exception handler's ability to handle exceptions and return appropriate error responses.
 */
@SuppressWarnings("nullness")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleNoSuchElementException_withMessage_returnsNotFoundStatus() {
        String errorMessage = "URL mapping not found";
        NoSuchElementException exception = new NoSuchElementException(errorMessage);

        ResponseEntity<?> response = handler.handleNoSuchElementException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleNoSuchElementException_returnsErrorDtoWithStatus() {
        NoSuchElementException exception = new NoSuchElementException("Not found");
        ResponseEntity<?> response = handler.handleNoSuchElementException(exception);

        assertNotNull(response.getBody());
        assertInstanceOf(ErrorDto.class, response.getBody());
        ErrorDto errorDto = (ErrorDto) response.getBody();
        assertEquals(HttpStatus.NOT_FOUND, errorDto.getStatus());
    }

    @Test
    @SuppressWarnings("nullness")
    void handleNoSuchElementException_returnsErrorDtoWithMessage() {
        String message = "Token not found in database";
        NoSuchElementException exception = new NoSuchElementException(message);

        ResponseEntity<?> response = handler.handleNoSuchElementException(exception);
        Object body = response.getBody();
        assertNotNull(body);
        assertInstanceOf(ErrorDto.class, body);
        ErrorDto errorDto = (ErrorDto) body;
        assertEquals(message, errorDto.getMessage());
    }

    @Test
    @SuppressWarnings("nullness")
    void handleNoSuchElementException_withoutMessage_returnsErrorDto() {
        NoSuchElementException exception = new NoSuchElementException();

        ResponseEntity<?> response = handler.handleNoSuchElementException(exception);

        Object body = response.getBody();
        assertNotNull(body);
        assertInstanceOf(ErrorDto.class, body);
        ErrorDto errorDto = (ErrorDto) body;
        assertNull(errorDto.getMessage());
    }

    @Test
    @SuppressWarnings("nullness")
    void handleNoSuchElementException_multipleExceptions_handlesEach() {
        NoSuchElementException exception1 = new NoSuchElementException("Error 1");
        NoSuchElementException exception2 = new NoSuchElementException("Error 2");

        ResponseEntity<?> response1 = handler.handleNoSuchElementException(exception1);
        ResponseEntity<?> response2 = handler.handleNoSuchElementException(exception2);

        Object body1 = response1.getBody();
        Object body2 = response2.getBody();
        assertNotNull(body1);
        assertNotNull(body2);
        ErrorDto dto1 = (ErrorDto) body1;
        ErrorDto dto2 = (ErrorDto) body2;

        assertEquals("Error 1", dto1.getMessage());
        assertEquals("Error 2", dto2.getMessage());
    }

    @Test
    @SuppressWarnings("nullness")
    void handleNoSuchElementException_emptyMessage_handlesGracefully() {
        NoSuchElementException exception = new NoSuchElementException("");

        ResponseEntity<?> response = handler.handleNoSuchElementException(exception);
        Object body = response.getBody();
        assertNotNull(body);
        ErrorDto errorDto = (ErrorDto) body;
        assertEquals("", errorDto.getMessage());
    }

    @Test
    @SuppressWarnings("nullness")
    void handleNoSuchElementException_nullMessage_handlesGracefully() {
        NoSuchElementException exception = new NoSuchElementException((String) null);

        ResponseEntity<?> response = handler.handleNoSuchElementException(exception);
        Object body = response.getBody();
        assertNotNull(body);
        ErrorDto errorDto = (ErrorDto) body;
        assertNull(errorDto.getMessage());
    }

    @Test
    @SuppressWarnings("nullness")
    void handleNoSuchElementException_longMessage_handlesGracefully() {
        String longMessage = "Token abc123xyz not found in the URL mapping database after searching all records";
        NoSuchElementException exception = new NoSuchElementException(longMessage);

        ResponseEntity<?> response = handler.handleNoSuchElementException(exception);
        Object body = response.getBody();
        assertNotNull(body);
        ErrorDto errorDto = (ErrorDto) body;
        assertEquals(longMessage, errorDto.getMessage());
    }
}
