# 🚀 Quick Start Guide - Women Safety Platform

This guide will help you get the Women Safety Alert & Emergency Response System up and running in minutes.

## ⚡ Prerequisites

- **Docker** & **Docker Compose** installed
- **Java 17** (if running services individually)
- **Maven 3.8+** (if building from source)
- **Git** for cloning the repository

## 🏃‍♀️ Quick Deployment

### 1. Clone and Start All Services

```bash
# Clone the repository
git clone <repository-url>
cd women-safety-platform

# Start all services with Docker Compose
docker-compose up -d

# Check if all services are running
docker-compose ps
```

### 2. Verify Services are Running

Wait for all services to start (approximately 2-3 minutes), then check:

```bash
# Check service health
curl http://localhost:8080/health  # API Gateway
curl http://localhost:8081/api/auth/health  # User Service
curl http://localhost:8082/actuator/health  # Alert Service

# View service logs
docker-compose logs -f api-gateway
docker-compose logs -f user-service
```

## 🎯 Test the System

### 1. Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "SecurePass123!",
    "firstName": "Jane",
    "lastName": "Doe",
    "phoneNumber": "+1234567890"
  }'
```

**Expected Response:**
```json
{
  "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "firstName": "Jane",
    "lastName": "Doe",
    "isActive": true,
    "isVerified": false
  }
}
```

### 2. Login User

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "SecurePass123!"
  }'
```

### 3. Create Emergency Alert

```bash
# Use the access token from login/registration
export ACCESS_TOKEN="your-access-token-here"

curl -X POST http://localhost:8080/api/alerts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -d '{
    "alertType": "SOS",
    "severity": "CRITICAL",
    "title": "Emergency Help Needed",
    "description": "I need immediate assistance",
    "latitude": 40.7128,
    "longitude": -74.0060,
    "address": "New York, NY"
  }'
```

### 4. Get User Alerts

```bash
curl -X GET http://localhost:8080/api/alerts \
  -H "Authorization: Bearer $ACCESS_TOKEN"
```

## 🌐 Access Web Interfaces

### Service Endpoints
- **API Gateway**: http://localhost:8080
- **User Service**: http://localhost:8081
- **Alert Service**: http://localhost:8082

### Infrastructure Services
- **RabbitMQ Management**: http://localhost:15672 (admin/admin123)
- **Zipkin Tracing**: http://localhost:9411

### API Documentation
- **User Service API**: http://localhost:8081/swagger-ui.html
- **Alert Service API**: http://localhost:8082/swagger-ui.html

## 📊 Database Access

### PostgreSQL Connection
```bash
# Connect to PostgreSQL
docker exec -it women-safety-postgres psql -U postgres -d women_safety

# List databases
\l

# Connect to user service database
\c user_service_db

# List tables
\dt

# View users
SELECT id, username, email, first_name, last_name, is_active, created_at FROM users;
```

### Redis Access
```bash
# Connect to Redis
docker exec -it women-safety-redis redis-cli

# Check keys
KEYS *

# Get a value
GET some_key
```

## 🔧 Configuration

### Environment Variables

Create a `.env` file in the project root:

```bash
# Database
DB_HOST=postgres
DB_PORT=5432
DB_USERNAME=postgres
DB_PASSWORD=postgres123

# Redis
REDIS_HOST=redis
REDIS_PORT=6379

# RabbitMQ
RABBITMQ_HOST=rabbitmq
RABBITMQ_USERNAME=admin
RABBITMQ_PASSWORD=admin123

# JWT
JWT_SECRET=womensafety-secret-key-for-jwt-token-generation-2024
JWT_EXPIRATION=86400000

# Email (Optional - for email notifications)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

## 🐛 Troubleshooting

### Common Issues

#### Services Not Starting
```bash
# Check logs for errors
docker-compose logs [service-name]

# Restart specific service
docker-compose restart [service-name]

# Clean restart all services
docker-compose down
docker-compose up -d
```

#### Database Connection Issues
```bash
# Check if PostgreSQL is running
docker-compose ps postgres

# Check PostgreSQL logs
docker-compose logs postgres

# Restart database
docker-compose restart postgres
```

#### Port Conflicts
```bash
# Check which ports are in use
netstat -tulpn | grep :8080

# Stop conflicting services
sudo systemctl stop conflicting-service

# Or change ports in docker-compose.yml
```

### Health Checks

```bash
# Check all service health
for port in 8080 8081 8082 8083 8084 8085 8086 8087 8888; do
  echo "Checking port $port:"
  curl -f http://localhost:$port/actuator/health 2>/dev/null || echo "Service on port $port is down"
done
```

## 📱 Testing with Postman

### Import Collection

1. Download Postman
2. Import the collection from `postman/Women-Safety-Platform.postman_collection.json`
3. Set environment variables:
   - `baseUrl`: http://localhost:8080
   - `accessToken`: (will be set automatically after login)

### Test Scenarios

1. **User Registration & Login**
2. **Create Emergency Alert**
3. **Update Alert Status**
4. **Get Alert History**
5. **Manage Emergency Contacts**

## 🔄 Development Workflow

### Running Individual Services

```bash
# Start infrastructure only
docker-compose up -d postgres redis rabbitmq zipkin

# Run services individually (in separate terminals)
cd user-service && mvn spring-boot:run
cd alert-service && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run
```

### Code Changes

```bash
# Rebuild specific service
docker-compose build user-service

# Restart with new build
docker-compose up -d user-service

# View logs
docker-compose logs -f user-service
```

## 📈 Monitoring

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f user-service

# With timestamps
docker-compose logs -f -t alert-service
```

### Check Metrics
```bash
# Service metrics
curl http://localhost:8081/actuator/metrics

# Health details
curl http://localhost:8081/actuator/health

# Application info
curl http://localhost:8081/actuator/info
```

## 🔐 Security Testing

### Authentication Flow
```bash
# 1. Register user
RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"SecurePass123!","firstName":"Jane","lastName":"Doe"}')

# 2. Extract token
TOKEN=$(echo $RESPONSE | jq -r '.accessToken')

# 3. Access protected endpoint
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/alerts
```

### Password Security Test
```bash
# Test weak password (should fail)
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"weak","email":"weak@example.com","password":"123","firstName":"Test","lastName":"User"}'
```

## 🛑 Stopping Services

```bash
# Stop all services
docker-compose down

# Stop and remove volumes (WARNING: This will delete all data)
docker-compose down -v

# Stop specific service
docker-compose stop user-service
```

## 🔄 Updates and Maintenance

### Update Services
```bash
# Pull latest images
docker-compose pull

# Restart with latest
docker-compose up -d

# Clean up old images
docker image prune
```

### Backup Database
```bash
# Backup all databases
docker exec women-safety-postgres pg_dumpall -U postgres > backup.sql

# Restore
docker exec -i women-safety-postgres psql -U postgres < backup.sql
```

## 📞 Need Help?

- **Check Logs**: `docker-compose logs -f [service-name]`
- **Health Status**: `curl http://localhost:8080/health`
- **API Documentation**: Visit `http://localhost:808X/swagger-ui.html` for each service
- **Database Issues**: Check PostgreSQL logs and connection settings
- **Port Conflicts**: Use `netstat -tulpn` to check port usage

## 🎉 Success Indicators

Your system is running correctly when:

✅ All services show "UP" status in health checks  
✅ You can register and login users  
✅ Emergency alerts can be created and retrieved  
✅ API documentation is accessible  
✅ No error logs in service containers  

---

**Ready to build something amazing? Start creating alerts and keeping people safe! 🛡️**