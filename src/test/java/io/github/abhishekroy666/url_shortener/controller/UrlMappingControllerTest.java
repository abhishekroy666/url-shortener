package io.github.abhishekroy666.url_shortener.controller;

import io.github.abhishekroy666.url_shortener.dto.UrlMappingDto;
import io.github.abhishekroy666.url_shortener.service.UrlMappingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for UrlMappingController.
 * Comprehensive tests for URL shortening and expansion REST endpoints.
 */
@ExtendWith(MockitoExtension.class)
class UrlMappingControllerTest {

    @Mock
    private UrlMappingService service;

    @InjectMocks
    private UrlMappingController controller;

    @Test
    void shorten_returnsBadRequest_whenLongUrlMissing() {
        UrlMappingDto dto = new UrlMappingDto();
        dto.setLongUrl("");
        ResponseEntity<UrlMappingDto> resp = controller.shorten(dto);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());

        dto.setLongUrl(null);
        resp = controller.shorten(dto);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    @Test
    void shorten_returnsOk_whenLongUrlPresent() {
        UrlMappingDto input = new UrlMappingDto();
        input.setLongUrl("https://ok.example/");

        UrlMappingDto returned = new UrlMappingDto();
        returned.setLongUrl(input.getLongUrl());
        returned.setToken("tkn");
        returned.setShortUrl("http://localhost/api/v1/tkn");

        when(service.shortenUrl(input.getLongUrl())).thenReturn(returned);

        ResponseEntity<UrlMappingDto> resp = controller.shorten(input);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(returned, resp.getBody());
    }

    @Test
    void expand_returnsBadRequest_whenTokenMissing() {
        ResponseEntity<UrlMappingDto> resp = controller.expand("");
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        resp = controller.expand(null);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    @Test
    void expand_returnsOk_whenTokenProvided() {
        String token = "abc";
        UrlMappingDto dto = new UrlMappingDto();
        dto.setToken(token);
        dto.setLongUrl("https://x");
        when(service.getUrlMapping(token)).thenReturn(dto);

        ResponseEntity<UrlMappingDto> resp = controller.expand(token);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(dto, resp.getBody());
    }

    @Test
    void shorten_whitespaceOnlyUrl_returnsBadRequest() {
        UrlMappingDto dto = new UrlMappingDto();
        dto.setLongUrl("   ");
        ResponseEntity<UrlMappingDto> resp = controller.shorten(dto);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    @Test
    void shorten_validUrl_callsService() {
        UrlMappingDto input = new UrlMappingDto();
        String url = "https://example.com";
        input.setLongUrl(url);

        UrlMappingDto output = new UrlMappingDto();
        output.setLongUrl(url);
        output.setToken("short");
        output.setShortUrl("http://localhost/api/v1/short");

        when(service.shortenUrl(url)).thenReturn(output);

        ResponseEntity<UrlMappingDto> resp = controller.shorten(input);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        verify(service, times(1)).shortenUrl(url);
    }

    @Test
    void expand_validToken_callsService() {
        String token = "token123";
        UrlMappingDto expected = new UrlMappingDto();
        expected.setToken(token);
        expected.setLongUrl("https://long.url");

        when(service.getUrlMapping(token)).thenReturn(expected);

        ResponseEntity<UrlMappingDto> resp = controller.expand(token);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        verify(service, times(1)).getUrlMapping(token);
    }

    @Test
    void expand_whitespaceToken_returnsBadRequest() {
        ResponseEntity<UrlMappingDto> resp = controller.expand("   ");
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"url1", "url2", "url3"})
    void shorten_multipleUrls_returnsCorrectResponses(String url) {
        UrlMappingDto input = new UrlMappingDto();
        input.setLongUrl(url);

        UrlMappingDto output = new UrlMappingDto();
        output.setLongUrl(url);
        output.setToken("token_" + url);

        when(service.shortenUrl(url)).thenReturn(output);

        ResponseEntity<UrlMappingDto> resp = controller.shorten(input);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        UrlMappingDto body = resp.getBody();
        assertNotNull(body);
        assertEquals(url, body.getLongUrl());
    }

    @ParameterizedTest
    @ValueSource(strings = {"token1", "token2", "token3"})
    void expand_multipleTokens_returnsCorrectResponses(String token) {
        UrlMappingDto expected = new UrlMappingDto();
        expected.setToken(token);
        expected.setLongUrl("https://url.com/" + token);

        when(service.getUrlMapping(token)).thenReturn(expected);

        ResponseEntity<UrlMappingDto> resp = controller.expand(token);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        UrlMappingDto body = resp.getBody();
        assertNotNull(body);
        assertEquals(token, body.getToken());
    }

    @Test
    void shorten_serviceThrowsException_exceptionPropagates() {
        UrlMappingDto input = new UrlMappingDto();
        input.setLongUrl("https://test.com");

        when(service.shortenUrl(anyString()))
            .thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () -> controller.shorten(input));
    }

    @Test
    void expand_serviceThrowsNoSuchElementException_exceptionPropagates() {
        String token = "missing";
        when(service.getUrlMapping(token))
            .thenThrow(new NoSuchElementException("Not found"));

        assertThrows(NoSuchElementException.class, () -> controller.expand(token));
    }

    @Test
    void shorten_longUrlWithSpecialCharacters_returnsOk() {
        UrlMappingDto input = new UrlMappingDto();
        String url = "https://example.com/path?param=value&other=123#fragment";
        input.setLongUrl(url);

        UrlMappingDto output = new UrlMappingDto();
        output.setLongUrl(url);
        output.setToken("special");

        when(service.shortenUrl(url)).thenReturn(output);

        ResponseEntity<UrlMappingDto> resp = controller.shorten(input);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

}
