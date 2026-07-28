CREATE TABLE IF NOT EXISTS doro_games (
    id UUID NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    removed BOOLEAN NOT NULL DEFAULT FALSE,
    player1_id UUID NOT NULL,
    player1_name VARCHAR(255) NOT NULL,
    player2_id UUID NOT NULL,
    player2_name VARCHAR(255) NOT NULL,
    field_variant VARCHAR(255) NOT NULL,
    win_checker_variant VARCHAR(255) NOT NULL,
    finished BOOLEAN NOT NULL,
    winner INTEGER NOT NULL,
    turn INTEGER NOT NULL
);
