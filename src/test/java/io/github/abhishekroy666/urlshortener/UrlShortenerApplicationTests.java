package io.github.abhishekroy666.urlshortener;

import io.github.abhishekroy666.urlshortener.service.UrlMappingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the URL Shortener application.
 * Tests the Spring Boot application context and full end-to-end functionality.
 */
@SpringBootTest
@Slf4j
class UrlShortenerApplicationTests {

	@Autowired(required = false)
	private UrlMappingService service;

	@Autowired(required = false)
	private CacheManager cacheManager;

	@Test
	void contextLoads() {
		log.info("Context loaded successfully.");
		assertNotNull(service, "UrlMappingService should be available");
	}

	@Test
	void applicationContext_loadsSuccessfully() {
		assertNotNull(service, "Service should be autowired");
	}

	@Test
	void cacheManager_isConfigured() {
		assertNotNull(cacheManager, "CacheManager should be configured for caching");
	}

	@Test
	void service_canShortenUrl_integration() {
		String longUrl = "https://integration-test.example.com/long/path";
		var result = service.shortenUrl(longUrl);

		assertNotNull(result);
		assertEquals(longUrl, result.getLongUrl());
		assertNotNull(result.getToken());
		assertNotNull(result.getShortUrl());
		assertTrue(result.getShortUrl().contains("/api/v1/"));
	}

	@Test
	void service_canRetrieveUrl_integration() {
		String longUrl = "https://retrieve-test.example.com/path";
		var shortenedResult = service.shortenUrl(longUrl);
		String token = shortenedResult.getToken();

		var retrievedResult = service.getUrlMapping(token);

		assertNotNull(retrievedResult);
		assertEquals(longUrl, retrievedResult.getLongUrl());
		assertEquals(token, retrievedResult.getToken());
	}

	@Test
	void service_reusesExistingUrl_integration() {
		String longUrl = "https://reuse-test.example.com/path";
		
		var result1 = service.shortenUrl(longUrl);
		var result2 = service.shortenUrl(longUrl);

		assertEquals(result1.getToken(), result2.getToken(), 
			"Same URL should generate same token");
	}

	@Test
	void applicationClass_hasSpringBootApplicationAnnotation() {
		Annotation[] anns = UrlShortenerApplication.class.getAnnotations();
		boolean found = false;
		for (Annotation a : anns) {
			if (a.annotationType().getName().contains("SpringBootApplication")) {
				found = true;
				break;
			}
		}
		assertTrue(found, "UrlShortenerApplication should be annotated with @SpringBootApplication");
	}

}
