CREATE TABLE game_results (
    id UUID NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    removed BOOLEAN NOT NULL DEFAULT FALSE,
    game_id UUID NOT NULL,
    winner_id UUID NOT NULL,
    loser_id UUID NOT NULL,
    finished_at TIMESTAMP NOT NULL
);
