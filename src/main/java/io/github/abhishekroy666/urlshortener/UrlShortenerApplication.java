package io.github.abhishekroy666.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main entry point for the URL Shortener Spring Boot application.
 * <p>
 * This class bootstraps the Spring context and enables caching for
 * short URL lookups. The application exposes REST endpoints for
 * creating and resolving shortened URLs.
 * </p>
 */
@SpringBootApplication
@EnableCaching
public class UrlShortenerApplication {

	/**
	 * Application entry point. Starts the embedded Spring Boot server.
	 *
	 * @param args command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApplication.class, args);
	}

}
