-- =============================================
-- api keys remove scopes from Table
-- =============================================

ALTER TABLE api_keys
    DROP COLUMN scopes;

-- =============================================
-- api keys scope Join Table
-- =============================================
CREATE TABLE api_keys_scope (
                                   api_keys_id UUID NOT NULL,
                                   api_key_scope VARCHAR(50) NOT NULL,

                                   CONSTRAINT fk_api_key_scopes
                                       FOREIGN KEY (api_keys_id)
                                           REFERENCES api_keys(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT uk_api_key_scopes
                                       UNIQUE (api_keys_id, api_key_scope)
);

CREATE INDEX idx_api_key_scope_api_keys_id ON api_keys_scope(api_keys_id);

-- =============================================
-- api keys add indexes to Table
-- =============================================

CREATE INDEX idx_api_keys_app_name ON api_keys(app_name);
CREATE INDEX IF NOT EXISTS idx_api_keys_active ON api_keys(active);