CREATE TABLE IF NOT EXISTS chip_move_entity (
    id UUID NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    removed BOOLEAN NOT NULL DEFAULT FALSE,
    chip_id UUID NOT NULL,
    move_idx INTEGER NOT NULL,
    x_after INTEGER NOT NULL,
    y_after INTEGER NOT NULL,
    CONSTRAINT fk_chip_move_entity_chip_id FOREIGN KEY (chip_id) REFERENCES start_positions (id),
    CONSTRAINT uq_chip_move_entity_chip_id_move_idx UNIQUE (chip_id, move_idx)
);
