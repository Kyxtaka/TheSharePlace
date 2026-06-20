CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- USERS
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    uuid UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ROLES
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    uuid UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    role_name VARCHAR(255) UNIQUE NOT NULL
);

-- USER <-> ROLE (global roles)
CREATE TABLE user_roles (
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES roles(role_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- GROUPS
CREATE TABLE groups (
    group_id SERIAL PRIMARY KEY,
    uuid UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL,
    hashed_vault_key VARCHAR(255),
    group_description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PLATFORMS
CREATE TABLE platforms (
    platform_id SERIAL PRIMARY KEY,
    uuid UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL,
    website_url VARCHAR(255),
    img_url VARCHAR(255)
);

-- SHARED ACCOUNTS
CREATE TABLE accounts (
    account_id SERIAL PRIMARY KEY,
    uuid UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    username VARCHAR(255),
    encrypted_password TEXT NOT NULL,
    email VARCHAR(255),
    a2f_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    platform_id INT NOT NULL REFERENCES platforms(platform_id) ON DELETE RESTRICT,
    group_id INT NOT NULL REFERENCES groups(group_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- GROUP <-> USER with scoped role
CREATE TABLE group_users (
    group_join_id SERIAL PRIMARY KEY,
    group_id INT NOT NULL REFERENCES groups(group_id) ON DELETE CASCADE,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES roles(role_id) ON DELETE RESTRICT,
    UNIQUE (group_id, user_id, role_id)
);

CREATE TABLE account_history (
    account_history_id SERIAL PRIMARY KEY,
    account_id INT NOT NULL REFERENCES accounts(account_id) ON DELETE CASCADE,
    changed_by_user_id INT REFERENCES users(user_id) ON DELETE SET NULL,
    change_type VARCHAR(50) NOT NULL CHECK (change_type IN ('created', 'updated', 'deleted')),
    change_description VARCHAR(255),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ARCHIVE (GDPR)
CREATE TABLE archived_accounts (
    archived_account_id SERIAL PRIMARY KEY,
    uuid UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    username VARCHAR(255),
    encrypted_password TEXT,
    email VARCHAR(255),
    a2f_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    platform_id INT REFERENCES platforms(platform_id) ON DELETE SET NULL,
    group_id INT REFERENCES groups(group_id) ON DELETE SET NULL,
    archived_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE group_join_requests (
    group_join_request_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    group_id INT NOT NULL REFERENCES groups(group_id) ON DELETE CASCADE,
    request_status VARCHAR(50) NOT NULL CHECK (request_status IN ('pending', 'approved', 'rejected')) DEFAULT 'pending',
    request_uuid UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    request_password VARCHAR(255),
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expire_at TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL '7 days'),
    revoked BOOLEAN DEFAULT FALSE
);

CREATE TABLE a2f_requests (
    a2f_request_id SERIAL PRIMARY KEY,
    account_id INT NOT NULL REFERENCES accounts(account_id) ON DELETE CASCADE,
    request_uuid UUID DEFAULT gen_random_uuid() UNIQUE NOT NULL,
    request_status VARCHAR(50) NOT NULL CHECK (request_status IN ('pending', 'approved', 'rejected')) DEFAULT 'pending',
    request_magic_token VARCHAR(255),
    request_pin_token VARCHAR(255),
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expire_at TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL '10 minutes'),
    revoked BOOLEAN DEFAULT FALSE
);

-- Indexes
CREATE INDEX idx_accounts_group_id ON accounts(group_id);
CREATE INDEX idx_accounts_platform_id ON accounts(platform_id);
CREATE INDEX idx_group_users_user_id ON group_users(user_id);
