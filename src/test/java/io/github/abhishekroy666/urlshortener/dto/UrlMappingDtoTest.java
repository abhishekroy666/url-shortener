package io.github.abhishekroy666.urlshortener.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UrlMappingDto.
 * Tests the DTO object creation, getters, and setters for URL mapping data.
 */
class UrlMappingDtoTest {

    private UrlMappingDto dto;

    @BeforeEach
    void setUp() {
        dto = new UrlMappingDto();
    }

    @Test
    void defaultConstructor_createsValidDto() {
        assertNotNull(dto);
        assertNull(dto.getLongUrl());
        assertNull(dto.getShortUrl());
        assertNull(dto.getToken());
    }

    @Test
    void setLongUrl_setsValueCorrectly() {
        String longUrl = "https://example.com/very/long/url";
        dto.setLongUrl(longUrl);
        assertEquals(longUrl, dto.getLongUrl());
    }

    @Test
    void setShortUrl_setsValueCorrectly() {
        String shortUrl = "http://localhost:8080/api/v1/abc123";
        dto.setShortUrl(shortUrl);
        assertEquals(shortUrl, dto.getShortUrl());
    }

    @Test
    void setToken_setsValueCorrectly() {
        String token = "abc123xyz";
        dto.setToken(token);
        assertEquals(token, dto.getToken());
    }

    @Test
    void allFields_setAndRetrievedCorrectly() {
        String longUrl = "https://example.com/path";
        String shortUrl = "http://localhost/api/v1/123";
        String token = "123";

        dto.setLongUrl(longUrl);
        dto.setShortUrl(shortUrl);
        dto.setToken(token);

        assertEquals(longUrl, dto.getLongUrl());
        assertEquals(shortUrl, dto.getShortUrl());
        assertEquals(token, dto.getToken());
    }

    @Test
    void setLongUrl_nullValue_allowsNull() {
        dto.setLongUrl(null);
        assertNull(dto.getLongUrl());
    }

    @Test
    void setShortUrl_nullValue_allowsNull() {
        dto.setShortUrl(null);
        assertNull(dto.getShortUrl());
    }

    @Test
    void setToken_nullValue_allowsNull() {
        dto.setToken(null);
        assertNull(dto.getToken());
    }

    @Test
    void setFields_multipleTimesWithDifferentValues_lastValuePersists() {
        dto.setLongUrl("url1");
        dto.setLongUrl("url2");
        assertEquals("url2", dto.getLongUrl());
    }

    @Test
    void emptyString_allowedForFields() {
        dto.setLongUrl("");
        dto.setShortUrl("");
        dto.setToken("");

        assertEquals("", dto.getLongUrl());
        assertEquals("", dto.getShortUrl());
        assertEquals("", dto.getToken());
    }

}

