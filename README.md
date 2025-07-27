# Authentication Service

A robust Spring Boot-based authentication service that provides JWT-based authentication and authorization with comprehensive error handling and logging.

## 🚀 Features

- **JWT Authentication** - Secure token-based authentication
- **User Registration & Login** - Complete user management
- **Token Refresh** - Automatic token renewal mechanism
- **Role-based Authorization** - Support for multiple user roles
- **Comprehensive Error Handling** - Proper HTTP status codes and error responses
- **Logging** - Detailed logging for monitoring and debugging
- **Unit Testing** - Complete test coverage for all components
- **Security** - BCrypt password encoding and secure token validation

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **Maven**
- **SLF4J Logging**
- **JUnit 5 & Mockito**

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use the included Maven wrapper)
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

The application will start on `http://localhost:8082`

## 📚 API Endpoints

| Endpoint | Method | Description | Authentication |
|----------|--------|-------------|----------------|
| `/api/health` | GET | Check service health | None |
| `/api/info` | GET | Get service information | None |
| `/api/auth/register` | POST | Register a new user | None |
| `/api/auth/login` | POST | Authenticate user | None |
| `/api/auth/refresh` | POST | Refresh access token | None |
| `/api/auth/protected` | GET | Test protected endpoint | JWT Required |

### Base URL
```
http://localhost:8082/api/auth
```

### 1. User Registration
**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "roles": ["USER"]
}
```

**Response (201 Created):**
```json
{
  "Access-Token": "eyJhbGciOiJIUzI1NiJ9...",
  "Refresh-Token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 2. User Login
**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "roles": ["USER"]
}
```

**Response (200 OK):**
```json
{
  "Access-Token": "eyJhbGciOiJIUzI1NiJ9...",
  "Refresh-Token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 3. Token Refresh
**Endpoint:** `POST /api/auth/refresh`

**Request Body:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response (200 OK):**
```json
{
  "Access-Token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 4. Protected Endpoint Example
**Endpoint:** `GET /api/auth/protected`

**Headers:**
```
Authorization: Bearer <access-token>
```

**Response (200 OK):**
```json
"this is protected"
```

## 🔐 Authentication Flow

1. **Registration**: User registers with email, password, and roles
2. **Login**: User authenticates and receives access and refresh tokens
3. **API Access**: Include `Authorization: Bearer <access-token>` header
4. **Token Refresh**: Use refresh token to get new access token when expired

## 🛡️ Security Features

- **BCrypt Password Encoding** - Secure password hashing
- **JWT Token Validation** - Secure token verification
- **Role-based Access Control** - Authorization based on user roles
- **Token Expiration** - Access tokens expire after 15 minutes
- **Refresh Token Rotation** - Secure token renewal mechanism

## 📊 Error Handling

The service provides comprehensive error handling with proper HTTP status codes:

| Error Type | HTTP Status | Description |
|------------|-------------|-------------|
| Validation Error | 400 | Invalid request data |
| Authentication Failed | 401 | Invalid credentials |
| User Not Found | 404 | User doesn't exist |
| User Already Exists | 409 | Duplicate registration |
| Invalid Token | 401 | Malformed JWT token |
| Token Expired | 401 | Expired JWT token |
| Internal Server Error | 500 | Unexpected server error |

### Error Response Format
```json
{
  "error": "Error type description",
  "message": "Detailed error message",
  "status": "HTTP status code"
}
```

## 📁 Project Structure

```
src/
├── main/java/com/example/auth_service/
│   ├── controller/
│   │   ├── UserController.java
│   │   ├── RefreshTokenController.java
│   │   └── TestingController.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── JwtService.java
│   │   ├── RefreshTokenService.java
│   │   └── MyUserDetailsService.java
│   ├── dto/
│   │   ├── UserDTO.java
│   │   └── RefreshTokenDTO.java
│   ├── model/
│   │   └── UserPrinciple.java
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── filter/
│   │   └── JwtFilter.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── UserAlreadyExistsException.java
│   │   ├── InvalidTokenException.java
│   │   └── TokenExpiredException.java
│   └── AuthServiceApplication.java
└── test/java/com/example/auth_service/
    ├── AuthServiceApplicationTests.java
    ├── UserServiceTest.java
    ├── UserControllerTest.java
    └── GlobalExceptionHandlerTest.java
```

## 🔧 Configuration

### JWT Configuration
- **Access Token Expiration**: 15 minutes
- **Refresh Token Expiration**: 7 days
- **Token Secret**: Configure in `application.properties`

### Security Configuration
- **Password Encoder**: BCrypt with strength 12
- **Session Management**: Stateless
- **CSRF**: Disabled for API usage
- **CORS**: Configure as needed

### Log Format
```
2024-01-15 10:30:45 - Registering user with email: user@example.com
2024-01-15 10:30:46 - User registered successfully: user@example.com
```

## 🚀 Deployment

### Docker
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/auth-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Variables
```bash
export JWT_SECRET=your-secure-secret-key
export SERVER_PORT=8080
export LOGGING_LEVEL=INFO
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the existing documentation
- Review the test cases for usage examples

## 🔄 Version History

- **v1.0.0** - Initial release with JWT authentication
- Added comprehensive error handling
- Implemented logging and unit testing
- Added token refresh functionality
