CREATE TABLE api_keys (
                          id              UUID PRIMARY KEY,
                          app_name        VARCHAR(255) NOT NULL,
                          key_hash        VARCHAR(255) NOT NULL UNIQUE,     -- Hashed value (Argon2/BCrypt recommended)
                          key_prefix      VARCHAR(32) NOT NULL,             -- e.g., first characters for reference
                          description     TEXT,
                          active          BOOLEAN NOT NULL DEFAULT TRUE,
                          scopes          TEXT[],
                          rate_limit      INTEGER DEFAULT 1000,             -- requests per minute, NULL = unlimited
                          created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          expired_at      TIMESTAMP,
                          revoked_at      TIMESTAMP,
                          revoked_reason  VARCHAR(255),
                          last_used_at    TIMESTAMP,
                          created_by      UUID,                             -- Optional: FK to users or admins table
                          CONSTRAINT fk_created_by FOREIGN KEY (created_by) REFERENCES user_entity(id)
);