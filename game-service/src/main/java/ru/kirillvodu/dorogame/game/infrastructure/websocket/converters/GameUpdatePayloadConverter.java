package ru.kirillvodu.dorogame.game.infrastructure.websocket.converters;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;
import ru.kirillvodu.dorogame.game.infrastructure.websocket.DTO.ChipsPositionsDTO;

@Component
public class GameUpdatePayloadConverter {
    public ChipsPositionsDTO convert(DoroGame game) {
        return new ChipsPositionsDTO(game.getPlayer1().getChips(), game.getPlayer2().getChips());
    }
}
