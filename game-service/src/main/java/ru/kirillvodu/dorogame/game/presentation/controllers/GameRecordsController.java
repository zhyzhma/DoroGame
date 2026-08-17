package ru.kirillvodu.dorogame.game.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kirillvodu.dorogame.game.application.abstractions.security.CurrentUserProvider;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.GameRecordDTO;
import ru.kirillvodu.dorogame.game.application.contracts.commands.GetGameRecordCommand;
import ru.kirillvodu.dorogame.game.application.mappers.GameRecordMapper;
import ru.kirillvodu.dorogame.game.application.services.GameRecordService;

import java.util.UUID;

@RestController
@RequestMapping("/records")
public class GameRecordsController {
    @Autowired
    private GameRecordService service;

    @Autowired
    private GameRecordMapper mapper;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @GetMapping("/{id}")
    public GameRecordDTO getGameRecord(@PathVariable UUID id) {
        UUID userId = currentUserProvider.getCurrentUserId();
        return mapper.toDTO(service.getGameRecord(new GetGameRecordCommand(id, userId)));
    }
}
