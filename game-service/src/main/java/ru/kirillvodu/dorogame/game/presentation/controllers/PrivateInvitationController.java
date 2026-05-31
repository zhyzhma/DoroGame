package ru.kirillvodu.dorogame.game.presentation.controllers;

import org.springframework.web.bind.annotation.*;
import ru.kirillvodu.dorogame.game.application.abstractions.security.CurrentUserProvider;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.create.PrivateInvitationCreateDTO;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.DoroGameDTO;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.InvitationReadDTO;
import ru.kirillvodu.dorogame.game.application.contracts.commands.AcceptPrivateInvitationCommand;
import ru.kirillvodu.dorogame.game.application.contracts.commands.CreatePrivateInvitationCommand;
import ru.kirillvodu.dorogame.game.application.mappers.DoroGameMapper;
import ru.kirillvodu.dorogame.game.application.mappers.InvitationMapper;
import ru.kirillvodu.dorogame.game.application.services.PrivateInvitationService;
import ru.kirillvodu.dorogame.game.domain.model.GameConfig;
import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/invitations/private")
public class PrivateInvitationController {

    private final PrivateInvitationService privateInvitationService;
    private final InvitationMapper invitationMapper;
    private final DoroGameMapper doroGameMapper;
    private final CurrentUserProvider currentUserProvider;

    public PrivateInvitationController(PrivateInvitationService privateInvitationService,
                                       InvitationMapper invitationMapper,
                                       DoroGameMapper doroGameMapper,
                                       CurrentUserProvider currentUserProvider) {
        this.privateInvitationService = privateInvitationService;
        this.invitationMapper = invitationMapper;
        this.doroGameMapper = doroGameMapper;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    public InvitationReadDTO create(@RequestBody PrivateInvitationCreateDTO request) {
        UUID userId = currentUserProvider.getCurrentUserId();
        GameConfig config = new GameConfig(
                FieldVariant.fromString(request.fieldVariant()),
                WinCheckerVariant.fromString(request.winCheckerVariant()),
                request.turn()
        );
        return invitationMapper.toDto(
                privateInvitationService.createPrivateInvitation(
                        new CreatePrivateInvitationCommand(userId, request.opponentId(), config))
        );
    }

    @GetMapping
    public List<InvitationReadDTO> getMy() {
        UUID userId = currentUserProvider.getCurrentUserId();
        return privateInvitationService.getInvitationsForUser(userId).stream()
                .map(invitationMapper::toDto)
                .toList();
    }

    @PostMapping("/{id}/accept")
    public DoroGameDTO accept(@PathVariable UUID id) {
        UUID userId = currentUserProvider.getCurrentUserId();
        return doroGameMapper.toDto(
                privateInvitationService.acceptPrivateInvitation(new AcceptPrivateInvitationCommand(id, userId))
        );
    }
}
