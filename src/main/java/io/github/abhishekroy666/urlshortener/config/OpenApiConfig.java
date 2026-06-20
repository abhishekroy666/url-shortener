package io.github.abhishekroy666.urlshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * OpenAPI configuration for the URL Shortener service.
 * Exposes an OpenAPI bean used by springdoc to generate the UI at /swagger-ui.html or /swagger-ui/index.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener Application")
                        .version("v1")
                        .description("API for shortening URLs and redirecting short links to original URLs.")
                        .contact(new Contact()
                                .name("Abhishek Roy")
                                .email("roy.ironmaiden@gmail.com")
                                .url(""))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }
}

