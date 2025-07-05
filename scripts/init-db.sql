-- Women Safety Platform Database Initialization Script

-- Create databases for each service
CREATE DATABASE user_service_db;
CREATE DATABASE alert_service_db;
CREATE DATABASE location_service_db;
CREATE DATABASE contact_service_db;
CREATE DATABASE notification_service_db;
CREATE DATABASE report_service_db;
CREATE DATABASE analytics_service_db;

-- Connect to user_service_db
\c user_service_db;

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    profile_picture_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User roles
CREATE TABLE user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Connect to alert_service_db
\c alert_service_db;

-- Emergency alerts table
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
    is_false_alarm BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Alert responses table
CREATE TABLE alert_responses (
    id BIGSERIAL PRIMARY KEY,
    alert_id BIGINT NOT NULL REFERENCES emergency_alerts(id) ON DELETE CASCADE,
    responder_id BIGINT NOT NULL,
    response_type VARCHAR(20) NOT NULL,
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Connect to location_service_db
\c location_service_db;

-- Location tracking table
CREATE TABLE location_tracking (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    accuracy DECIMAL(5, 2),
    altitude DECIMAL(7, 2),
    speed DECIMAL(5, 2),
    heading DECIMAL(5, 2),
    address TEXT,
    is_safe_zone BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Safe zones table
CREATE TABLE safe_zones (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    radius DECIMAL(8, 2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Connect to contact_service_db
\c contact_service_db;

-- Emergency contacts table
CREATE TABLE emergency_contacts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    relationship VARCHAR(50),
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    priority_level INTEGER DEFAULT 1,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Connect to notification_service_db
\c notification_service_db;

-- Notifications table
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    alert_id BIGINT,
    type VARCHAR(20) NOT NULL,
    title VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    channel VARCHAR(20) NOT NULL,
    recipient VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    sent_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Connect to report_service_db
\c report_service_db;

-- Incident reports table
CREATE TABLE incident_reports (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    incident_type VARCHAR(50) NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    address TEXT,
    severity VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'SUBMITTED',
    evidence_urls TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Connect to analytics_service_db
\c analytics_service_db;

-- Safety analytics table
CREATE TABLE safety_analytics (
    id BIGSERIAL PRIMARY KEY,
    area_name VARCHAR(100),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    incident_count INTEGER DEFAULT 0,
    alert_count INTEGER DEFAULT 0,
    safety_score DECIMAL(3, 2),
    analysis_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
\c user_service_db;
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);

\c alert_service_db;
CREATE INDEX idx_alerts_user_id ON emergency_alerts(user_id);
CREATE INDEX idx_alerts_status ON emergency_alerts(status);
CREATE INDEX idx_alerts_created_at ON emergency_alerts(created_at);

\c location_service_db;
CREATE INDEX idx_location_user_id ON location_tracking(user_id);
CREATE INDEX idx_location_created_at ON location_tracking(created_at);
CREATE INDEX idx_safe_zones_user_id ON safe_zones(user_id);

\c contact_service_db;
CREATE INDEX idx_contacts_user_id ON emergency_contacts(user_id);
CREATE INDEX idx_contacts_priority ON emergency_contacts(priority_level);

\c notification_service_db;
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_status ON notifications(status);

\c report_service_db;
CREATE INDEX idx_reports_user_id ON incident_reports(user_id);
CREATE INDEX idx_reports_status ON incident_reports(status);

\c analytics_service_db;
CREATE INDEX idx_analytics_date ON safety_analytics(analysis_date);
CREATE INDEX idx_analytics_location ON safety_analytics(latitude, longitude);