package io.github.abhishekroy666.url_shortener.dto;

import io.github.abhishekroy666.url_shortener.entity.UrlMapping;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DtoEntityTests {

    @Test
    void errorDto_accessors_work() {
        ErrorDto e = new ErrorDto();
        e.setStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
        e.setMessage("bad");
        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST, e.getStatus());
        assertEquals("bad", e.getMessage());
    }

    @Test
    void urlMappingDto_accessors_work() {
        UrlMappingDto dto = new UrlMappingDto();
        dto.setLongUrl("https://x");
        dto.setShortUrl("http://s");
        dto.setToken("t");
        assertEquals("https://x", dto.getLongUrl());
        assertEquals("http://s", dto.getShortUrl());
        assertEquals("t", dto.getToken());
    }

    @Test
    void entity_accessors_work() {
        UrlMapping m = new UrlMapping();
        m.setId(77L);
        m.setLongUrl("https://entity");
        m.setCreatedAt(LocalDateTime.now());
        assertEquals(77L, m.getId());
        assertEquals("https://entity", m.getLongUrl());
        assertNotNull(m.getCreatedAt());
    }
}

