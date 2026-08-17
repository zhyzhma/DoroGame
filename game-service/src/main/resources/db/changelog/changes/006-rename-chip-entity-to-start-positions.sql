ALTER TABLE chip_entity RENAME TO start_positions;
ALTER TABLE start_positions RENAME CONSTRAINT fk_chip_entity_game_id TO fk_start_positions_game_id;
CREATE INDEX IF NOT EXISTS idx_start_positions_game_id ON start_positions (game_id);
