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

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for RedirectController.
 * Comprehensive tests for URL redirect functionality with all exception paths covered.
 */
@ExtendWith(MockitoExtension.class)
class RedirectControllerTest {

    @Mock
    private UrlMappingService service;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private RedirectController controller;

    @Test
    void redirect_sendsRedirect_whenTokenFound() throws Exception {
        String token = "x";
        UrlMappingDto dto = new UrlMappingDto();
        dto.setLongUrl("https://redirect.example/");
        when(service.getUrlMapping(token)).thenReturn(dto);

        controller.redirect(token, response);

        verify(response).sendRedirect(dto.getLongUrl());
    }

    @Test
    void redirect_sendsError_whenServiceThrows() throws Exception {
        String token = "y";
        when(service.getUrlMapping(token)).thenThrow(new NoSuchElementException("not found"));

        controller.redirect(token, response);

        verify(response).sendError(eq(404), anyString());
    }

    @Test
    void redirect_sendsError_whenSendRedirectThrows() throws Exception {
        String token = "z";
        UrlMappingDto dto = new UrlMappingDto();
        dto.setLongUrl("https://boom.example/");
        when(service.getUrlMapping(token)).thenReturn(dto);
        doThrow(new IOException("io error")).when(response).sendRedirect(dto.getLongUrl());

        controller.redirect(token, response);

        verify(response).sendError(eq(404), anyString());
    }

    @Test
    void redirect_logsInfo_whenRedirectSuccessful() throws Exception {
        String token = "logtest";
        String longUrl = "https://example.com/test";
        UrlMappingDto dto = new UrlMappingDto();
        dto.setLongUrl(longUrl);

        when(service.getUrlMapping(token)).thenReturn(dto);

        controller.redirect(token, response);

        verify(response).sendRedirect(longUrl);
    }

    @Test
    void redirect_logsError_whenTokenNotFound() throws Exception {
        String token = "missing";
        when(service.getUrlMapping(token)).thenThrow(new NoSuchElementException("Token not found"));

        controller.redirect(token, response);

        verify(response).sendError(404, "Token not found");
    }

    @Test
    void redirect_sendsErrorWithCorrectStatusCode() throws Exception {
        String token = "error";
        when(service.getUrlMapping(token)).thenThrow(new NoSuchElementException("test"));

        controller.redirect(token, response);

        verify(response).sendError(eq(404), anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"token1", "token2", "token3"})
    void redirect_redirectsToCorrectUrl(String token) throws Exception {
        UrlMappingDto dto = new UrlMappingDto();
        String expectedUrl = "https://example.com/" + token;
        dto.setLongUrl(expectedUrl);

        when(service.getUrlMapping(token)).thenReturn(dto);

        controller.redirect(token, response);

        verify(response).sendRedirect(expectedUrl);
    }

    @Test
    void redirect_handlesEmptyToken_sendsError() throws Exception {
        String token = "";
        when(service.getUrlMapping(token)).thenThrow(new NoSuchElementException("Empty token"));

        controller.redirect(token, response);

        verify(response).sendError(eq(404), anyString());
    }

    @Test
    void redirect_handlesNullDto_throwsException() {
        String token = "null";
        when(service.getUrlMapping(token)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> controller.redirect(token, response));
    }

    @Test
    void redirect_handlesUrlWithSpecialCharacters() throws Exception {
        String token = "special";
        String longUrl = "https://example.com/path?param=value&other=123#fragment";
        UrlMappingDto dto = new UrlMappingDto();
        dto.setLongUrl(longUrl);

        when(service.getUrlMapping(token)).thenReturn(dto);

        controller.redirect(token, response);

        verify(response).sendRedirect(longUrl);
    }

    @Test
    void redirect_multipleSuccessfulRedirects_redirectsEach() throws Exception {
        String token1 = "t1";
        String token2 = "t2";

        UrlMappingDto dto1 = new UrlMappingDto();
        dto1.setLongUrl("https://url1.com");

        UrlMappingDto dto2 = new UrlMappingDto();
        dto2.setLongUrl("https://url2.com");

        when(service.getUrlMapping(token1)).thenReturn(dto1);
        when(service.getUrlMapping(token2)).thenReturn(dto2);

        controller.redirect(token1, response);
        controller.redirect(token2, response);

        verify(response).sendRedirect("https://url1.com");
        verify(response).sendRedirect("https://url2.com");
    }

    @Test
    void redirect_exceptionMessage_includedInErrorResponse() throws Exception {
        String token = "error";
        String errorMessage = "Specific error message";
        when(service.getUrlMapping(token)).thenThrow(new NoSuchElementException(errorMessage));

        controller.redirect(token, response);

        verify(response).sendError(eq(404), contains(errorMessage));
    }

}
