package ru.kirillvodu.dorogame.game.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;

import java.util.UUID;

@Service
public class GameAccessChecker {

    @Autowired
    private DoroGameRepository doroGameRepository;

    public boolean isUserInGame(UUID userId, UUID gameId) {
        DoroGame game = doroGameRepository.getById(gameId)
                .orElseThrow(() -> new ObjectNotFoundException(gameId, "DoroGame"));
        UUID player1 = game.getPlayer1().getUser().id();
        UUID player2 = game.getPlayer2().getUser().id();
        return userId.equals(player1) || userId.equals(player2);
    }
}
