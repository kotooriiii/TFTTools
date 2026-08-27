CREATE TABLE users
(
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username         VARCHAR(255) NOT NULL,
    email            VARCHAR(255) NOT NULL,
    password_hash    VARCHAR(255),
    oauth_provider   VARCHAR(50),
    oauth_subject_id VARCHAR(255),
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX users_email_key ON users (email);
CREATE UNIQUE INDEX users_oauth_provider_subject_id_key ON users (oauth_provider, oauth_subject_id);
