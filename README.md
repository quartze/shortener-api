# Shortener URL

### Documentation
Live: `https://api.shortener.wavecloud.pl/`
Live docs: `https://api.shortener.wavecloud.pl/swagger-ui/index.html`

### Description
The Shortener URL API is a REST API system for creating and managing short URLs.
* Users can create short URLs anonymously or while logged in.
* Logged-in users can:
  * View a list of their created short URLs.
  * Access usage statistics, including a counter for every request made to `GET /shorts/{id}`.
  * Delete shorts that are created by them


### Auth
* Authentication is handled using JWT tokens with a Refresh Token strategy.
* Authentication endpoints are available under the `/auth` route.
* Passwords are stored securely using the `BCrypt` hashing strategy.
* Getting current logged user data via `/me` endpoint

### AuthUtils
An additional library (`helpers` folder) is included for handling authentication processes such as:
* Generating user secret keys.
* Generating tokens

The methods in this library are well-documented to explain their functionality.

### Swagger documentation
Swagger documentation is accessible via `/swagger-ui/index.html`

### Structure
* `/config`: Configuration files.
* `/exceptions`: Custom exceptions for API error handling.
* `/filters`: Filters for Spring Security Lifecycle
* `/helpers`: Utility tools and methods (e.g., AuthUtils for authentication).
* `/models`: Database models for the application's data structure.
* `/repositories`: JPA repository interfaces for database operations.
* `/requests`: POJO representations for REST API requests.
* `/routes`: API route handlers.
* `/services`: Service layer containing business logic and advanced database operations.

### Need Api?
You can reach me at email (look my github page).

### Frameworks
* Spring Boot
* Spring Security
* JWT
* Java JWT
* JPA/Hibernate
* Swagger
* MySQL
* Lombok
