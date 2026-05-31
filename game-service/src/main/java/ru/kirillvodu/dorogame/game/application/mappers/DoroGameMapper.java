package ru.kirillvodu.dorogame.game.application.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.DoroGameDTO;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;

@Component
public class DoroGameMapper {

    public DoroGameDTO toDto(DoroGame game) {
        return new DoroGameDTO(
                game.getId(),
                game.getPlayer1().getUser().id(),
                game.getPlayer1().getUser().name(),
                game.getPlayer2().getUser().id(),
                game.getPlayer2().getUser().name(),
                game.isFinished(),
                game.getWinner(),
                game.getTurn()
        );
    }
}
