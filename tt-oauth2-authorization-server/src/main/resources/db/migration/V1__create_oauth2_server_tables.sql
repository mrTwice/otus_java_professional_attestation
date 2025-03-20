CREATE TABLE oauth2_client
(
    id                            VARCHAR(255) PRIMARY KEY,
    client_id                     VARCHAR(255) NOT NULL UNIQUE,
    client_id_issued_at           TIMESTAMP    NOT NULL,
    client_secret                 TEXT,
    client_secret_expires_at      TIMESTAMP,
    client_name                   VARCHAR(255) NOT NULL,
    client_authentication_methods VARCHAR(500) NOT NULL,
    authorization_grant_types     VARCHAR(500) NOT NULL,
    redirect_uris                 TEXT,
    scopes                        VARCHAR(500) NOT NULL,
    client_settings               TEXT NOT NULL,
    token_settings                TEXT NOT NULL
);

CREATE TABLE oauth2_authorization
(
    id                            VARCHAR(255) PRIMARY KEY,
    registered_client_id          VARCHAR(255) NOT NULL,
    principal_name                VARCHAR(255) NOT NULL,
    authorization_grant_type      VARCHAR(255) NOT NULL,
    authorized_scopes             TEXT,
    attributes                    TEXT,
    state                         VARCHAR(500),
    authorization_code_value      TEXT,
    authorization_code_issued_at  TIMESTAMP,
    authorization_code_expires_at TIMESTAMP,
    authorization_code_metadata   TEXT,
    access_token_value            TEXT UNIQUE,
    access_token_issued_at        TIMESTAMP,
    access_token_expires_at       TIMESTAMP,
    access_token_metadata         TEXT,
    access_token_type             VARCHAR(255),
    access_token_scopes           TEXT,
    refresh_token_value           TEXT UNIQUE,
    refresh_token_issued_at       TIMESTAMP,
    refresh_token_expires_at      TIMESTAMP,
    refresh_token_metadata        TEXT,
    FOREIGN KEY (registered_client_id) REFERENCES oauth2_client (id) ON DELETE CASCADE
);

CREATE INDEX idx_oauth2_authorization_principal_name ON oauth2_authorization(principal_name);
CREATE INDEX idx_oauth2_authorization_grant_type ON oauth2_authorization(authorization_grant_type);
CREATE INDEX idx_oauth2_authorization_access_token ON oauth2_authorization(access_token_value);
CREATE INDEX idx_oauth2_authorization_refresh_token ON oauth2_authorization(refresh_token_value);
