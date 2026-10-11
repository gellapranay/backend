-- Antarmarg Database Schema - V1 Initial Schema
-- Universal Intelligent Internal Navigation & Last-Meter Delivery Platform

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_approved BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE,
    vehicle_type VARCHAR(100),
    vehicle_number VARCHAR(100),
    service_area VARCHAR(255),
    organization VARCHAR(255),
    facility_access_request VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS facilities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    facility_type VARCHAR(50) NOT NULL,
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    boundary_json TEXT,
    hierarchy_schema_json TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS facility_admins (
    id BIGSERIAL PRIMARY KEY,
    facility_id BIGINT NOT NULL REFERENCES facilities(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_facility_user UNIQUE (facility_id, user_id)
);

CREATE TABLE IF NOT EXISTS location_nodes (
    id BIGSERIAL PRIMARY KEY,
    facility_id BIGINT NOT NULL REFERENCES facilities(id) ON DELETE CASCADE,
    parent_node_id BIGINT REFERENCES location_nodes(id) ON DELETE SET NULL,
    name VARCHAR(255) NOT NULL,
    node_type VARCHAR(50) NOT NULL,
    level_number INTEGER DEFAULT 0,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    floor_plan_x DOUBLE PRECISION,
    floor_plan_y DOUBLE PRECISION,
    metadata_json TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_nodes_facility ON location_nodes(facility_id);
CREATE INDEX IF NOT EXISTS idx_nodes_parent ON location_nodes(parent_node_id);
CREATE INDEX IF NOT EXISTS idx_nodes_type ON location_nodes(node_type);

CREATE TABLE IF NOT EXISTS location_edges (
    id BIGSERIAL PRIMARY KEY,
    facility_id BIGINT NOT NULL REFERENCES facilities(id) ON DELETE CASCADE,
    source_node_id BIGINT NOT NULL REFERENCES location_nodes(id) ON DELETE CASCADE,
    destination_node_id BIGINT NOT NULL REFERENCES location_nodes(id) ON DELETE CASCADE,
    distance_meters DOUBLE PRECISION NOT NULL DEFAULT 10.0,
    estimated_time_seconds INTEGER NOT NULL DEFAULT 15,
    is_accessible BOOLEAN DEFAULT TRUE,
    allow_vehicles BOOLEAN DEFAULT FALSE,
    is_bidirectional BOOLEAN DEFAULT TRUE,
    status VARCHAR(50) DEFAULT 'OPEN',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_edges_facility ON location_edges(facility_id);
CREATE INDEX IF NOT EXISTS idx_edges_source ON location_edges(source_node_id);
CREATE INDEX IF NOT EXISTS idx_edges_dest ON location_edges(destination_node_id);

CREATE TABLE IF NOT EXISTS deliveries (
    id BIGSERIAL PRIMARY KEY,
    tracking_number VARCHAR(100) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL REFERENCES users(id),
    delivery_partner_id BIGINT REFERENCES users(id),
    facility_id BIGINT NOT NULL REFERENCES facilities(id),
    destination_node_id BIGINT NOT NULL REFERENCES location_nodes(id),
    status VARCHAR(50) NOT NULL,
    recipient_name VARCHAR(255) NOT NULL,
    recipient_phone VARCHAR(50) NOT NULL,
    delivery_description TEXT,
    landmark VARCHAR(255),
    access_instructions TEXT,
    preferred_time_window VARCHAR(100),
    recommended_gate_id BIGINT REFERENCES location_nodes(id),
    total_distance_meters DOUBLE PRECISION,
    estimated_time_seconds INTEGER,
    actual_duration_seconds INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_at TIMESTAMP,
    accepted_at TIMESTAMP,
    started_at TIMESTAMP,
    arrived_at TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_deliveries_customer ON deliveries(customer_id);
CREATE INDEX IF NOT EXISTS idx_deliveries_partner ON deliveries(delivery_partner_id);
CREATE INDEX IF NOT EXISTS idx_deliveries_facility ON deliveries(facility_id);
CREATE INDEX IF NOT EXISTS idx_deliveries_status ON deliveries(status);

CREATE TABLE IF NOT EXISTS obstacles (
    id BIGSERIAL PRIMARY KEY,
    facility_id BIGINT NOT NULL REFERENCES facilities(id) ON DELETE CASCADE,
    location_node_id BIGINT REFERENCES location_nodes(id) ON DELETE SET NULL,
    location_edge_id BIGINT REFERENCES location_edges(id) ON DELETE SET NULL,
    obstacle_type VARCHAR(50) NOT NULL,
    description TEXT,
    severity VARCHAR(50) DEFAULT 'MEDIUM',
    status VARCHAR(50) DEFAULT 'ACTIVE',
    reported_by_id BIGINT REFERENCES users(id),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    photo_url VARCHAR(500),
    expires_at TIMESTAMP,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_obstacles_facility ON obstacles(facility_id);
CREATE INDEX IF NOT EXISTS idx_obstacles_status ON obstacles(status);

CREATE TABLE IF NOT EXISTS route_feedbacks (
    id BIGSERIAL PRIMARY KEY,
    delivery_id BIGINT NOT NULL REFERENCES deliveries(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id),
    rating INTEGER NOT NULL,
    comment TEXT,
    difficulty_score INTEGER,
    was_accurate BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_events (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    action VARCHAR(255) NOT NULL,
    entity_type VARCHAR(100),
    entity_id VARCHAR(100),
    details TEXT,
    ip_address VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
