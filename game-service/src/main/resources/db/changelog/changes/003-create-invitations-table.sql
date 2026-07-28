CREATE TABLE IF NOT EXISTS invitations (
    id UUID NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    removed BOOLEAN NOT NULL DEFAULT FALSE,
    user_id UUID NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    target_user_id UUID,
    field_variant VARCHAR(255) NOT NULL,
    win_checker_variant VARCHAR(255) NOT NULL,
    turn INTEGER NOT NULL
);
