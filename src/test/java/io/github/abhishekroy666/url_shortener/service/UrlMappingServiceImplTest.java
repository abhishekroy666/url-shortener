package io.github.abhishekroy666.url_shortener.service;

import io.github.abhishekroy666.url_shortener.dto.UrlMappingDto;
import io.github.abhishekroy666.url_shortener.entity.UrlMapping;
import io.github.abhishekroy666.url_shortener.repository.UrlMappingRepository;
import io.github.abhishekroy666.url_shortener.util.Base62Encoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for UrlMappingServiceImpl.
 * Comprehensive tests for URL shortening and retrieval with full branch coverage.
 */
@ExtendWith(MockitoExtension.class)
class UrlMappingServiceImplTest {

    @Mock
    private UrlMappingRepository repository;

    @InjectMocks
    private UrlMappingServiceImpl service;

    @Captor
    private ArgumentCaptor<UrlMapping> mappingCaptor;

    @BeforeEach
    void setUp() {
        // Set up servlet request context for ServletUriComponentsBuilder
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("http");
        request.setServerName("localhost");
        request.setServerPort(8080);
        request.setContextPath("");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @AfterEach
    void tearDown() {
        // Clean up request context
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void shortenUrl_returnsExistingMapping_whenFound() {
        UrlMapping existing = new UrlMapping();
        existing.setId(5L);
        existing.setLongUrl("https://example.com/x");

        when(repository.findByLongUrl(existing.getLongUrl())).thenReturn(Optional.of(existing));

        UrlMappingDto dto = service.shortenUrl(existing.getLongUrl());

        assertEquals(existing.getLongUrl(), dto.getLongUrl());
        assertEquals(Base62Encoder.encode(5L), dto.getToken());
        assertTrue(dto.getShortUrl().endsWith("/api/v1/" + dto.getToken()));
        verify(repository, never()).save(any());
    }

    @Test
    void shortenUrl_savesNewEntity_whenNotFound() {
        String longUrl = "https://new.example/path";
        UrlMapping saved = new UrlMapping();
        saved.setId(42L);
        saved.setLongUrl(longUrl);

        when(repository.findByLongUrl(longUrl)).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(saved);

        UrlMappingDto dto = service.shortenUrl(longUrl);

        assertEquals(longUrl, dto.getLongUrl());
        assertEquals(Base62Encoder.encode(42L), dto.getToken());
        assertTrue(dto.getShortUrl().endsWith("/api/v1/" + dto.getToken()));
        verify(repository).save(mappingCaptor.capture());
        UrlMapping captured = mappingCaptor.getValue();
        assertEquals(longUrl, captured.getLongUrl());
    }

    @Test
    void getUrlMapping_returnsDto_whenFound() {
        UrlMapping mapping = new UrlMapping();
        mapping.setId(7L);
        mapping.setLongUrl("https://host/test");

        String token = Base62Encoder.encode(7L);
        when(repository.findById(7L)).thenReturn(Optional.of(mapping));

        UrlMappingDto dto = service.getUrlMapping(token);

        assertEquals(mapping.getLongUrl(), dto.getLongUrl());
        assertEquals(token, dto.getToken());
        assertTrue(dto.getShortUrl().endsWith("/api/v1/" + token));
    }

    @Test
    void getUrlMapping_throws_whenNotFound() {
        String token = Base62Encoder.encode(9999L);
        when(repository.findById(9999L)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> service.getUrlMapping(token));
        assertTrue(ex.getMessage().contains(token));
    }

    @Test
    void shortenUrl_newUrl_createsNewMapping() {
        String longUrl = "https://example.com/very/long/url";
        UrlMapping savedMapping = new UrlMapping();
        savedMapping.setId(1L);
        savedMapping.setLongUrl(longUrl);
        savedMapping.setCreatedAt(LocalDateTime.now());

        when(repository.findByLongUrl(longUrl)).thenReturn(Optional.empty());
        when(repository.save(any(UrlMapping.class))).thenReturn(savedMapping);

        UrlMappingDto result = service.shortenUrl(longUrl);

        assertNotNull(result);
        assertEquals(longUrl, result.getLongUrl());
        assertEquals("b", result.getToken()); // Base62 encoding of 1
        assertTrue(result.getShortUrl().endsWith("/api/v1/b"));
        verify(repository, times(1)).findByLongUrl(longUrl);
        verify(repository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    void shortenUrl_returnsDtoWithAllFields() {
        String longUrl = "https://example.com/test";
        UrlMapping mapping = new UrlMapping();
        mapping.setId(100L);
        mapping.setLongUrl(longUrl);

        when(repository.findByLongUrl(longUrl)).thenReturn(Optional.of(mapping));

        UrlMappingDto result = service.shortenUrl(longUrl);

        assertNotNull(result.getLongUrl());
        assertNotNull(result.getToken());
        assertNotNull(result.getShortUrl());
        assertEquals(longUrl, result.getLongUrl());
    }

    @Test
    void getUrlMapping_validToken_returnsMapping() {
        String token = "b"; // Decodes to ID 1
        UrlMapping mapping = new UrlMapping();
        mapping.setId(1L);
        mapping.setLongUrl("https://example.com/path");

        when(repository.findById(1L)).thenReturn(Optional.of(mapping));

        UrlMappingDto result = service.getUrlMapping(token);

        assertNotNull(result);
        assertEquals("https://example.com/path", result.getLongUrl());
        assertEquals(token, result.getToken());
        assertTrue(result.getShortUrl().endsWith("/api/v1/b"));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getUrlMapping_invalidToken_throwsNoSuchElementException() {
        String token = "xyz";
        long decodedId = Base62Encoder.decode(token);

        when(repository.findById(decodedId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getUrlMapping(token));
    }

    @Test
    void getUrlMapping_exceptionMessage_containsToken() {
        String token = "notfound";
        long decodedId = Base62Encoder.decode(token);

        when(repository.findById(decodedId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, 
            () -> service.getUrlMapping(token));
        assertTrue(exception.getMessage().contains(token));
    }

    @Test
    void getUrlMapping_largeId_decodesAndRetrieves() {
        String token = Base62Encoder.encode(999999L);
        UrlMapping mapping = new UrlMapping();
        mapping.setId(999999L);
        mapping.setLongUrl("https://large.id/url");

        when(repository.findById(999999L)).thenReturn(Optional.of(mapping));

        UrlMappingDto result = service.getUrlMapping(token);

        assertNotNull(result);
        assertEquals("https://large.id/url", result.getLongUrl());
        verify(repository, times(1)).findById(999999L);
    }

    @Test
    void shortenUrl_multipleNewUrls_savesEachSeparately() {
        UrlMapping mapping1 = new UrlMapping();
        mapping1.setId(1L);
        mapping1.setLongUrl("url1");

        UrlMapping mapping2 = new UrlMapping();
        mapping2.setId(2L);
        mapping2.setLongUrl("url2");

        when(repository.findByLongUrl("url1")).thenReturn(Optional.empty());
        when(repository.findByLongUrl("url2")).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(mapping1).thenReturn(mapping2);

        service.shortenUrl("url1");
        service.shortenUrl("url2");

        verify(repository, times(2)).save(any(UrlMapping.class));
    }

    @Test
    void shortenUrl_emptyString_savedAsUrl() {
        UrlMapping mapping = new UrlMapping();
        mapping.setId(5L);
        mapping.setLongUrl("");

        when(repository.findByLongUrl("")).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(mapping);

        UrlMappingDto result = service.shortenUrl("");

        assertEquals("", result.getLongUrl());
        verify(repository, times(1)).save(any());
    }

    @Test
    void getUrlMapping_zeroToken_decodesAndRetrieves() {
        String token = "a"; // Decodes to 0
        UrlMapping mapping = new UrlMapping();
        mapping.setId(0L);
        mapping.setLongUrl("https://zero.id/url");

        when(repository.findById(0L)).thenReturn(Optional.of(mapping));

        UrlMappingDto result = service.getUrlMapping(token);

        assertNotNull(result);
        assertEquals("https://zero.id/url", result.getLongUrl());
        verify(repository, times(1)).findById(0L);
    }

    @Test
    void shortenUrl_capturesUrlMappingWithCorrectUrl() {
        String testUrl = "https://test.example.com";
        UrlMapping savedMapping = new UrlMapping();
        savedMapping.setId(1L);
        savedMapping.setLongUrl(testUrl);

        when(repository.findByLongUrl(testUrl)).thenReturn(Optional.empty());
        when(repository.save(any(UrlMapping.class))).thenReturn(savedMapping);

        service.shortenUrl(testUrl);

        verify(repository).save(mappingCaptor.capture());
        assertEquals(testUrl, mappingCaptor.getValue().getLongUrl());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 5L, 10L, 100L, 1000L, 999999L})
    void shortenUrl_multipleIds_generatesCorrectTokens(long id) {
        String url = "https://example.com/" + id;
        UrlMapping mapping = new UrlMapping();
        mapping.setId(id);
        mapping.setLongUrl(url);

        when(repository.findByLongUrl(url)).thenReturn(Optional.of(mapping));

        UrlMappingDto result = service.shortenUrl(url);

        assertEquals(Base62Encoder.encode(id), result.getToken());
    }

    @ParameterizedTest
    @ValueSource(strings = {"b", "c", "abc", "xyz"})
    void getUrlMapping_multipleTokens_decodesCorrectly(String token) {
        long decodedId = Base62Encoder.decode(token);
        UrlMapping mapping = new UrlMapping();
        mapping.setId(decodedId);
        mapping.setLongUrl("https://test.com");

        when(repository.findById(decodedId)).thenReturn(Optional.of(mapping));

        UrlMappingDto result = service.getUrlMapping(token);

        assertEquals(token, result.getToken());
        verify(repository).findById(decodedId);
    }

}
