package ru.kirillvodu.dorogame.game.application.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.DoroGameDTO;
import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;

@Component
public class DoroGameMapper {

    @Autowired
    private ChipMapper chipMapper;

    public DoroGameDTO toDto(DoroGame game) {
        return new DoroGameDTO(
                game.getId(),
                game.getPlayer1().getUser().id(),
                game.getPlayer1().getUser().name(),
                game.getPlayer2().getUser().id(),
                game.getPlayer2().getUser().name(),
                chipMapper.toDTO(game.getPlayer1().getChips()),
                chipMapper.toDTO(game.getPlayer2().getChips()),
                game.isFinished(),
                game.getWinner(),
                game.getTurn()
        );
    }
}
