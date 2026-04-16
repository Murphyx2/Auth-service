CREATE EXTENSION IF NOT EXISTS citext;

-- =============================================
-- Main User Entity Table
-- =============================================
CREATE TABLE user_entity (
    id UUID PRIMARY KEY,
    username varchar(255) not null,
    email CITEXT not null unique,
    password_hash varchar(255) not null,
    first_name varchar(255),
    last_name varchar(255),
    created_date  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date  TIMESTAMPTZ
)

-- =============================================
-- Roles Join Table (for @ElementCollection)
-- =============================================
CREATE TABLE user_entity_roles (
    user_entity_id  UUID NOT NULL,
    roles           VARCHAR(50) NOT NULL,   -- Stores enum as string: 'ROLE_USER', 'ROLE_ADMIN'

    CONSTRAINT fk_user_roles
       FOREIGN KEY (user_entity_id)
       REFERENCES user_entity(id)
       ON DELETE CASCADE,

   CONSTRAINT uk_user_roles
       UNIQUE (user_entity_id, roles)
);
--Add indexes for performance
CREATE INDEX idx_user_entity_email ON user_entity(email);
CREATE INDEX idx_user_roles_user_id ON user_entity_roles(user_entity_id);