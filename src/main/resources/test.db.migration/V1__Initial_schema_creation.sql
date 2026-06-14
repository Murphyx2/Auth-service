-- =============================================
-- Main User Entity Table
-- =============================================
CREATE TABLE user_entity (
                             id UUID PRIMARY KEY,
                             username VARCHAR(255) NOT NULL,
                             email VARCHAR(255) NOT NULL UNIQUE,
                             password_hash VARCHAR(255) NOT NULL,
                             first_name VARCHAR(255),
                             last_name VARCHAR(255),
                             active BOOLEAN NOT NULL DEFAULT TRUE,
                             created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_date TIMESTAMPTZ
);

-- =============================================
-- Roles Join Table
-- =============================================
CREATE TABLE user_entity_roles (
                                   user_entity_id UUID NOT NULL,
                                   roles VARCHAR(50) NOT NULL,

                                   CONSTRAINT fk_user_roles
                                       FOREIGN KEY (user_entity_id)
                                           REFERENCES user_entity(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT uk_user_roles
                                       UNIQUE (user_entity_id, roles)
);

-- Performance indexes
CREATE INDEX idx_user_entity_email ON user_entity(email);
CREATE INDEX idx_user_roles_user_id ON user_entity_roles(user_entity_id);
CREATE INDEX IF NOT EXISTS idx_user_entity_active ON user_entity(active);