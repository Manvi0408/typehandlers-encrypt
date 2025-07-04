# Women Safety Alert & Emergency Response System - Complete Overview

## 🚀 System Architecture

This is a comprehensive microservices-based women safety platform built with Java Spring Boot that provides real-time emergency alert management, location tracking, and multi-channel notification capabilities.

### 📊 Architecture Diagram

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Client    │    │  Mobile App     │    │  Admin Panel    │
│   (React/Vue)   │    │  (React Native) │    │   (Angular)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                ┌─────────────────────────────────┐
                │         API Gateway             │
                │    (Spring Cloud Gateway)       │
                │  • Authentication & Security    │
                │  • Rate Limiting & CORS         │
                │  • Load Balancing & Routing     │
                └─────────────────────────────────┘
                                 │
         ┌───────────────────────────────────────────────────────┐
         │                                                       │
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│  User Service   │  │  Alert Service  │  │ Location Service│  │ Contact Service │
│   Port: 8081    │  │   Port: 8082    │  │   Port: 8083    │  │   Port: 8084    │
└─────────────────┘  └─────────────────┘  └─────────────────┘  └─────────────────┘
         │                       │                       │                       │
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│Notification Svc │  │  Report Service │  │Analytics Service│  │  Config Service │
│   Port: 8085    │  │   Port: 8086    │  │   Port: 8087    │  │   Port: 8888    │
└─────────────────┘  └─────────────────┘  └─────────────────┘  └─────────────────┘
         │                       │                       │                       │
         └───────────────────────────────────────────────────────────────────────┘
                                         │
                ┌─────────────────────────────────────────────────────────┐
                │                Infrastructure                           │
                │                                                         │
                │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
                │  │ PostgreSQL  │  │    Redis    │  │  RabbitMQ   │     │
                │  │ (Database)  │  │   (Cache)   │  │ (Messages)  │     │
                │  └─────────────┘  └─────────────┘  └─────────────┘     │
                │                                                         │
                │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
                │  │   Zipkin    │  │   Eureka    │  │   Docker    │     │
                │  │ (Tracing)   │  │ (Discovery) │  │(Containers) │     │
                │  └─────────────┘  └─────────────┘  └─────────────┘     │
                └─────────────────────────────────────────────────────────┘
```

## 🏗️ Microservices Architecture

### 1. **API Gateway Service** (Port: 8080)
- **Purpose**: Central entry point for all client requests
- **Features**:
  - JWT-based authentication and authorization
  - Rate limiting and DDoS protection
  - CORS configuration
  - Circuit breaker pattern for resilience
  - Request/response logging and monitoring

### 2. **User Service** (Port: 8081)
- **Purpose**: User management and authentication
- **Features**:
  - User registration and login
  - Email verification and password reset
  - Profile management
  - Role-based access control
  - Account security (lockout, failed attempts)
  - JWT token generation and validation

### 3. **Alert Service** (Port: 8082)
- **Purpose**: Emergency alert creation and management
- **Features**:
  - SOS button functionality
  - Multi-type alerts (harassment, assault, medical emergency, etc.)
  - Real-time alert broadcasting via WebSocket
  - Alert escalation and acknowledgment
  - Alert history and analytics
  - Integration with notification and location services

### 4. **Location Service** (Port: 8083)
- **Purpose**: Real-time location tracking and geofencing
- **Features**:
  - GPS location tracking
  - Safe zone management
  - Geofence alerts
  - Location history
  - Privacy controls
  - Integration with emergency services

### 5. **Contact Service** (Port: 8084)
- **Purpose**: Emergency contact management
- **Features**:
  - Emergency contact CRUD operations
  - Hierarchical contact prioritization
  - Contact group management
  - Auto-notification to emergency contacts
  - Contact verification

### 6. **Notification Service** (Port: 8085)
- **Purpose**: Multi-channel notification management
- **Features**:
  - SMS notifications
  - Email notifications
  - Push notifications
  - Voice call alerts
  - Notification templates
  - Delivery tracking and retry logic

### 7. **Report Service** (Port: 8086)
- **Purpose**: Incident reporting and case management
- **Features**:
  - Incident report creation
  - Evidence upload and management
  - Case status tracking
  - Report analytics
  - Integration with authorities

### 8. **Analytics Service** (Port: 8087)
- **Purpose**: Safety analytics and insights
- **Features**:
  - Safety heat maps
  - Incident trend analysis
  - User behavior analytics
  - Safety score calculations
  - Reporting dashboards

### 9. **Config Service** (Port: 8888)
- **Purpose**: Centralized configuration management
- **Features**:
  - Environment-specific configurations
  - Dynamic configuration updates
  - Service discovery integration
  - Configuration versioning

## 🔧 Technology Stack

### Backend Technologies
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **Message Queue**: RabbitMQ 3
- **Security**: Spring Security + JWT
- **API Gateway**: Spring Cloud Gateway
- **Service Discovery**: Netflix Eureka
- **Distributed Tracing**: Zipkin
- **Documentation**: OpenAPI 3.0

### DevOps & Deployment
- **Containerization**: Docker
- **Container Orchestration**: Docker Compose
- **Build Tool**: Maven
- **Monitoring**: Spring Boot Actuator + Micrometer
- **Health Checks**: Built-in health endpoints

## 🚀 Key Features

### 🆘 Emergency Alert System
- **One-Click SOS**: Instant emergency alert with location
- **Multi-Type Alerts**: Support for various emergency types
- **Real-Time Broadcasting**: WebSocket-based real-time alerts
- **Auto-Escalation**: Automatic escalation if no response
- **Geo-Location**: GPS-based location sharing

### 🔐 Security & Authentication
- **JWT-Based Auth**: Secure token-based authentication
- **Role-Based Access**: User, Admin, Moderator, Emergency Responder roles
- **Account Security**: Failed login protection, account lockout
- **Data Encryption**: Encrypted sensitive data storage
- **CORS Protection**: Cross-origin request security

### 📱 Multi-Channel Notifications
- **SMS Alerts**: Immediate SMS notifications
- **Email Notifications**: Detailed email alerts
- **Push Notifications**: Real-time mobile push notifications
- **Voice Calls**: Emergency voice call alerts
- **Notification Retry**: Automatic retry on failure

### 📍 Location & Geofencing
- **Real-Time Tracking**: Live GPS location tracking
- **Safe Zones**: Customizable safe area definitions
- **Geofence Alerts**: Automatic alerts when entering/leaving areas
- **Location History**: Historical location data
- **Privacy Controls**: User-controlled location sharing

### 📊 Analytics & Reporting
- **Safety Heat Maps**: Visual representation of incident data
- **Trend Analysis**: Pattern recognition in incidents
- **Response Time Analytics**: Emergency response metrics
- **User Behavior Insights**: Platform usage analytics
- **Custom Reports**: Configurable reporting system

## 📋 Database Schema

### User Service Database
```sql
-- Users table with security fields
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    is_verified BOOLEAN DEFAULT FALSE,
    failed_login_attempts INTEGER DEFAULT 0,
    account_locked_until TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User roles for RBAC
CREATE TABLE user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Alert Service Database
```sql
-- Emergency alerts with comprehensive tracking
CREATE TABLE emergency_alerts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    alert_type VARCHAR(20) NOT NULL,
    severity VARCHAR(10) NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    address TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    escalation_level INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Alert responses for tracking acknowledgments
CREATE TABLE alert_responses (
    id BIGSERIAL PRIMARY KEY,
    alert_id BIGINT REFERENCES emergency_alerts(id),
    responder_id BIGINT NOT NULL,
    response_type VARCHAR(20) NOT NULL,
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🐳 Docker Deployment

### Quick Start with Docker Compose
```bash
# Clone the repository
git clone <repository-url>
cd women-safety-platform

# Start all services
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f [service-name]

# Stop all services
docker-compose down
```

### Service Endpoints
- **API Gateway**: http://localhost:8080
- **User Service**: http://localhost:8081
- **Alert Service**: http://localhost:8082
- **Location Service**: http://localhost:8083
- **Contact Service**: http://localhost:8084
- **Notification Service**: http://localhost:8085
- **Report Service**: http://localhost:8086
- **Analytics Service**: http://localhost:8087
- **Config Service**: http://localhost:8888

### Infrastructure Services
- **PostgreSQL**: localhost:5432
- **Redis**: localhost:6379
- **RabbitMQ**: localhost:15672 (admin/admin123)
- **Zipkin**: http://localhost:9411

## 📚 API Documentation

Each service provides comprehensive API documentation:
- **Swagger UI**: Available at `/swagger-ui.html` for each service
- **OpenAPI Spec**: Available at `/v3/api-docs` for each service

### Key API Endpoints

#### Authentication APIs
```
POST /api/auth/register          - User registration
POST /api/auth/login             - User login
POST /api/auth/refresh           - Token refresh
GET  /api/auth/verify-email      - Email verification
POST /api/auth/forgot-password   - Password reset request
POST /api/auth/reset-password    - Password reset
```

#### Alert APIs
```
POST /api/alerts                 - Create emergency alert
GET  /api/alerts                 - Get user alerts
GET  /api/alerts/{id}            - Get specific alert
PUT  /api/alerts/{id}/status     - Update alert status
POST /api/alerts/{id}/response   - Respond to alert
```

#### Location APIs
```
POST /api/locations              - Update location
GET  /api/locations/current      - Get current location
POST /api/locations/safe-zones   - Create safe zone
GET  /api/locations/safe-zones   - Get safe zones
```

## 🔄 System Workflows

### Emergency Alert Flow
1. **User triggers SOS** → Alert Service creates emergency alert
2. **Location captured** → Location Service provides GPS coordinates
3. **Contacts retrieved** → Contact Service gets emergency contacts
4. **Notifications sent** → Notification Service sends multi-channel alerts
5. **Real-time broadcast** → WebSocket broadcasts to connected clients
6. **Escalation timer** → Auto-escalation if no response within 5 minutes
7. **Resolution tracking** → Status updates and resolution logging

### User Registration Flow
1. **User submits registration** → User Service validates data
2. **Password encrypted** → BCrypt hashing for security
3. **Verification email sent** → Email Service sends verification link
4. **Account created** → User record created with default USER role
5. **JWT tokens generated** → Access and refresh tokens provided

## 🔒 Security Features

### Authentication & Authorization
- **JWT-based authentication** with access and refresh tokens
- **Role-based access control** (USER, ADMIN, MODERATOR, EMERGENCY_RESPONDER)
- **Password strength validation** with complexity requirements
- **Account lockout protection** after failed login attempts
- **Email verification** for account activation

### Data Protection
- **Password hashing** using BCrypt
- **Sensitive data encryption** at rest and in transit
- **SQL injection prevention** with parameterized queries
- **CORS configuration** for cross-origin protection
- **Rate limiting** to prevent abuse

## 📈 Monitoring & Observability

### Health Monitoring
- **Spring Boot Actuator** endpoints for health checks
- **Custom health indicators** for database and external services
- **Prometheus metrics** for monitoring
- **Distributed tracing** with Zipkin

### Logging
- **Structured logging** with logback
- **Request/response logging** in API Gateway
- **Error tracking** with detailed stack traces
- **Audit logging** for security events

## 🚀 Deployment Options

### Local Development
```bash
# Start infrastructure services
docker-compose up -d postgres redis rabbitmq zipkin

# Run each service individually
cd user-service && mvn spring-boot:run
cd alert-service && mvn spring-boot:run
# ... repeat for other services
```

### Production Deployment
```bash
# Build all services
mvn clean package -DskipTests

# Deploy with Docker Compose
docker-compose -f docker-compose.prod.yml up -d

# Or deploy to Kubernetes
kubectl apply -f k8s/
```

## 🧪 Testing

### Unit Tests
- **JUnit 5** for unit testing
- **Mockito** for mocking
- **Spring Boot Test** for integration testing
- **Testcontainers** for database testing

### API Testing
- **Postman collections** for API testing
- **REST Assured** for automated API tests
- **Contract testing** with Spring Cloud Contract

## 📋 Configuration

### Environment Variables
```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_USERNAME=postgres
DB_PASSWORD=postgres123

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379

# RabbitMQ Configuration
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=admin
RABBITMQ_PASSWORD=admin123

# JWT Configuration
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000

# Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

## 🔮 Future Enhancements

### Planned Features
- **AI-powered risk assessment** for incident prediction
- **Integration with emergency services** for direct dispatch
- **Mobile app development** with React Native
- **Voice-activated alerts** for hands-free emergency calls
- **Machine learning** for pattern recognition in safety data
- **Blockchain integration** for immutable incident records
- **Multi-language support** for global deployment

### Technical Improvements
- **Kubernetes deployment** for production scalability
- **Circuit breaker implementation** for better resilience
- **Caching strategies** for improved performance
- **Advanced monitoring** with ELK stack
- **Performance optimization** and load testing
- **Security hardening** and penetration testing

## 🤝 Contributing

### Development Setup
1. **Prerequisites**: Java 17, Maven 3.8+, Docker, PostgreSQL
2. **Clone repository**: `git clone <repository-url>`
3. **Start infrastructure**: `docker-compose up -d postgres redis rabbitmq`
4. **Run services**: Use IDE or Maven to start each service
5. **Test**: Run unit tests with `mvn test`

### Code Standards
- **Java Code Style**: Follow Google Java Style Guide
- **Commit Messages**: Use conventional commits format
- **Documentation**: Update README and API docs for changes
- **Testing**: Write unit and integration tests for new features

## 📞 Support

For support and questions:
- **Email**: support@womensafety.com
- **Documentation**: Check service-specific README files
- **Issues**: Create GitHub issues for bugs and feature requests
- **Wiki**: Check project wiki for detailed documentation

---

**Built with ❤️ for women's safety and security**