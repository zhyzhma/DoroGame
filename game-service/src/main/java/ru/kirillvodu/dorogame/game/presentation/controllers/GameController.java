package ru.kirillvodu.dorogame.game.presentation.controllers;

import org.springframework.web.bind.annotation.*;
import ru.kirillvodu.dorogame.game.application.abstractions.security.CurrentUserProvider;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.create.MakeMoveDTO;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.DoroGameDTO;
import ru.kirillvodu.dorogame.game.application.contracts.commands.MakeMoveCommand;
import ru.kirillvodu.dorogame.game.application.contracts.results.MoveResult;
import ru.kirillvodu.dorogame.game.application.mappers.DoroGameMapper;
import ru.kirillvodu.dorogame.game.application.services.GameService;
import ru.kirillvodu.dorogame.game.domain.model.game.Coords;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final DoroGameMapper doroGameMapper;
    private final CurrentUserProvider currentUserProvider;

    public GameController(GameService gameService,
                          DoroGameMapper doroGameMapper,
                          CurrentUserProvider currentUserProvider) {
        this.gameService = gameService;
        this.doroGameMapper = doroGameMapper;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/finished")
    public List<DoroGameDTO> getFinishedGames() {
        UUID playerId = currentUserProvider.getCurrentUserId();
        return gameService.getByUserIdAndFinishedTrue(playerId).stream().map(doroGameMapper::toDto).toList();
    }

    @PostMapping("/{id}/moves")
    public MoveResult makeMove(@PathVariable UUID id,
                               @RequestBody MakeMoveDTO request) {
        UUID playerId = currentUserProvider.getCurrentUserId();
        MakeMoveCommand command = new MakeMoveCommand(id, playerId, request.chipId(),
                new Coords(request.x(), request.y()));
        return gameService.makeMove(command);
    }
}
