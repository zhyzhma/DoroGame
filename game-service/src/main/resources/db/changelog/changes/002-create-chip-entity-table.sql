CREATE TABLE chip_entity (
    id UUID NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    removed BOOLEAN NOT NULL DEFAULT FALSE,
    coord_x INTEGER NOT NULL,
    coord_y INTEGER NOT NULL,
    player_number INTEGER NOT NULL,
    game_id UUID NOT NULL,
    CONSTRAINT fk_chip_entity_game_id FOREIGN KEY (game_id) REFERENCES doro_games (id)
);
