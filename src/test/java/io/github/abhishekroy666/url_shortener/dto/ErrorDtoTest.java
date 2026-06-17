package io.github.abhishekroy666.url_shortener.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ErrorDto.
 * Tests the error DTO object creation, getters, and setters.
 */
class ErrorDtoTest {

    private ErrorDto errorDto;

    @BeforeEach
    void setUp() {
        errorDto = new ErrorDto();
    }

    @Test
    void defaultConstructor_createsValidErrorDto() {
        assertNotNull(errorDto);
        assertNull(errorDto.getStatus());
        assertNull(errorDto.getMessage());
    }

    @Test
    void setStatus_setsHttpStatusCorrectly() {
        errorDto.setStatus(HttpStatus.NOT_FOUND);
        assertEquals(HttpStatus.NOT_FOUND, errorDto.getStatus());
    }

    @Test
    void setMessage_setsMessageCorrectly() {
        String message = "Resource not found";
        errorDto.setMessage(message);
        assertEquals(message, errorDto.getMessage());
    }

    @Test
    void setStatusAndMessage_bothValuesSetCorrectly() {
        String message = "The requested resource could not be found";
        errorDto.setStatus(HttpStatus.NOT_FOUND);
        errorDto.setMessage(message);

        assertEquals(HttpStatus.NOT_FOUND, errorDto.getStatus());
        assertEquals(message, errorDto.getMessage());
    }

    @Test
    void setStatus_differentStatuses_onlyLastPersists() {
        errorDto.setStatus(HttpStatus.BAD_REQUEST);
        errorDto.setStatus(HttpStatus.NOT_FOUND);
        assertEquals(HttpStatus.NOT_FOUND, errorDto.getStatus());
    }

    @Test
    void setStatus_nullValue_allowsNull() {
        errorDto.setStatus(null);
        assertNull(errorDto.getStatus());
    }

    @Test
    void setMessage_nullValue_allowsNull() {
        errorDto.setMessage(null);
        assertNull(errorDto.getMessage());
    }

    @Test
    void setMessage_multipleStatuses_worksCorrectly() {
        errorDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        errorDto.setMessage("Server error");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorDto.getStatus());
        assertEquals("Server error", errorDto.getMessage());

        errorDto.setStatus(HttpStatus.UNAUTHORIZED);
        errorDto.setMessage("Unauthorized access");
        assertEquals(HttpStatus.UNAUTHORIZED, errorDto.getStatus());
        assertEquals("Unauthorized access", errorDto.getMessage());
    }

    @Test
    void setMessage_emptyString_allowedForMessage() {
        errorDto.setMessage("");
        assertEquals("", errorDto.getMessage());
    }

    @Test
    void setMessage_longMessage_allowedForMessage() {
        String longMessage = "This is a very long error message that explains in detail what went wrong with the request.";
        errorDto.setMessage(longMessage);
        assertEquals(longMessage, errorDto.getMessage());
    }

    @Test
    void allHttpStatuses_canBeSet() {
        for (HttpStatus status : HttpStatus.values()) {
            errorDto.setStatus(status);
            assertEquals(status, errorDto.getStatus());
        }
    }

}

