# URL Shortener

A Java-based Spring Boot application that provides URL shortening and redirection services.

## Description

This is a REST API application built with Spring Boot that allows users to:
- Shorten long URLs into compact, shareable links
- Store and manage URL mappings
- Redirect from short URLs to their original destinations
- Track and manage shortened URLs

## Tech Stack

- **Framework**: Spring Boot
- **Language**: Java
- **Build Tool**: Maven/Gradle
- **Database**: (Configure based on your setup)

## Features

- 🔗 URL shortening with custom or auto-generated short codes
- 📊 URL redirection and management
- 🔍 URL lookup and tracking
- ⚡ Fast and efficient short URL generation
- 🛡️ Input validation and error handling

## Prerequisites

- Java 11 or higher
- Maven 3.6+ or Gradle 6.0+
- Git

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/abhishekroy666/url-shortener.git
   cd url-shortener
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```
   or
   ```bash
   gradle clean build
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   or
   ```bash
   gradle bootRun
   ```

   The application will start on `http://localhost:8080`

## API Endpoints

### Create a Short URL
- **Endpoint**: `POST /api/shorten`
- **Request Body**:
  ```json
  {
    "originalUrl": "https://example.com/very/long/url"
  }
  ```
- **Response**:
  ```json
  {
    "shortUrl": "http://localhost:8080/abc123",
    "originalUrl": "https://example.com/very/long/url"
  }
  ```

### Redirect to Original URL
- **Endpoint**: `GET /{shortCode}`
- **Response**: Redirect to original URL (HTTP 302)

### Get URL Details
- **Endpoint**: `GET /api/urls/{shortCode}`
- **Response**:
  ```json
  {
    "shortCode": "abc123",
    "originalUrl": "https://example.com/very/long/url",
    "createdAt": "2026-06-15T10:30:00Z"
  }
  ```

## Configuration

Configure application properties in `application.properties` or `application.yml`:

```properties
# Server
server.port=8080

# Database (if applicable)
spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener
spring.datasource.username=root
spring.datasource.password=your_password
```

## Project Structure

```
url-shortener/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yourpackage/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       └── Application.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml (or build.gradle)
└── README.md
```

## Usage Example

### Using cURL

**Shorten a URL:**
```bash
curl -X POST http://localhost:8080/api/shorten \
  -H "Content-Type: application/json" \
  -d '{"originalUrl":"https://github.com/abhishekroy666/url-shortener"}'
```

**Access shortened URL:**
```bash
curl -L http://localhost:8080/abc123
```

## Testing

Run tests using Maven:
```bash
mvn test
```

Or with Gradle:
```bash
gradle test
```

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

[abhishekroy666](https://github.com/abhishekroy666)

## Support

For issues, questions, or suggestions, please open an [issue](https://github.com/abhishekroy666/url-shortener/issues).

---

**Last Updated**: June 2026
