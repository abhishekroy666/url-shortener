package io.github.abhishekroy666.url_shortener.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UrlMapping entity.
 * Tests the JPA entity object creation, getters, and setters.
 */
class UrlMappingTest {

    private UrlMapping urlMapping;

    @BeforeEach
    void setUp() {
        urlMapping = new UrlMapping();
    }

    @Test
    void defaultConstructor_createsValidEntity() {
        assertNotNull(urlMapping);
        assertNull(urlMapping.getId());
        assertNull(urlMapping.getLongUrl());
        assertNotNull(urlMapping.getCreatedAt());
    }

    @Test
    void setId_setsIdCorrectly() {
        Long id = 12345L;
        urlMapping.setId(id);
        assertEquals(id, urlMapping.getId());
    }

    @Test
    void setLongUrl_setsUrlCorrectly() {
        String longUrl = "https://example.com/path";
        urlMapping.setLongUrl(longUrl);
        assertEquals(longUrl, urlMapping.getLongUrl());
    }

    @Test
    void createdAt_defaultsToCurrentTime() {
        LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);
        UrlMapping mapping = new UrlMapping();
        LocalDateTime afterCreation = LocalDateTime.now().plusSeconds(1);

        assertTrue(mapping.getCreatedAt().isAfter(beforeCreation),
            "createdAt should be after before time");
        assertTrue(mapping.getCreatedAt().isBefore(afterCreation),
            "createdAt should be before after time");
    }

    @Test
    void setCreatedAt_setsTimeCorrectly() {
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        urlMapping.setCreatedAt(time);
        assertEquals(time, urlMapping.getCreatedAt());
    }

    @Test
    void allFields_setAndRetrievedCorrectly() {
        Long id = 999L;
        String longUrl = "https://example.com/very/long/url";
        LocalDateTime createdAt = LocalDateTime.now();

        urlMapping.setId(id);
        urlMapping.setLongUrl(longUrl);
        urlMapping.setCreatedAt(createdAt);

        assertEquals(id, urlMapping.getId());
        assertEquals(longUrl, urlMapping.getLongUrl());
        assertEquals(createdAt, urlMapping.getCreatedAt());
    }

    @Test
    void setId_nullValue_allowsNull() {
        urlMapping.setId(null);
        assertNull(urlMapping.getId());
    }

    @Test
    void setLongUrl_nullValue_allowsNull() {
        urlMapping.setLongUrl(null);
        assertNull(urlMapping.getLongUrl());
    }

    @Test
    void setLongUrl_emptyString_allowedForUrl() {
        urlMapping.setLongUrl("");
        assertEquals("", urlMapping.getLongUrl());
    }

    @Test
    void setId_differentValues_lastValuePersists() {
        urlMapping.setId(1L);
        urlMapping.setId(2L);
        assertEquals(2L, urlMapping.getId());
    }

    @Test
    void setId_largeValue_storedCorrectly() {
        Long largeId = Long.MAX_VALUE;
        urlMapping.setId(largeId);
        assertEquals(largeId, urlMapping.getId());
    }

    @Test
    void setLongUrl_veryLongUrl_storedCorrectly() {
        String longUrl = "https://example.com/" + "a".repeat(1000);
        urlMapping.setLongUrl(longUrl);
        assertEquals(longUrl, urlMapping.getLongUrl());
    }

}

