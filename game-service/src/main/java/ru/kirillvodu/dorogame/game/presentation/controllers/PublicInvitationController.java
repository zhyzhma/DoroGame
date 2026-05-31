package ru.kirillvodu.dorogame.game.presentation.controllers;

import org.springframework.web.bind.annotation.*;
import ru.kirillvodu.dorogame.game.application.abstractions.security.CurrentUserProvider;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.create.InvitationCreateDTO;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.DoroGameDTO;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.InvitationReadDTO;
import ru.kirillvodu.dorogame.game.application.contracts.commands.AcceptPublicInvitationCommand;
import ru.kirillvodu.dorogame.game.application.contracts.commands.CreatePublicInvitationCommand;
import ru.kirillvodu.dorogame.game.application.mappers.DoroGameMapper;
import ru.kirillvodu.dorogame.game.application.mappers.InvitationMapper;
import ru.kirillvodu.dorogame.game.application.services.PublicInvitationService;
import ru.kirillvodu.dorogame.game.domain.model.GameConfig;
import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/invitations/public")
public class PublicInvitationController {

    private final PublicInvitationService publicInvitationService;
    private final InvitationMapper invitationMapper;
    private final DoroGameMapper doroGameMapper;
    private final CurrentUserProvider currentUserProvider;

    public PublicInvitationController(PublicInvitationService publicInvitationService,
                                      InvitationMapper invitationMapper,
                                      DoroGameMapper doroGameMapper,
                                      CurrentUserProvider currentUserProvider) {
        this.publicInvitationService = publicInvitationService;
        this.invitationMapper = invitationMapper;
        this.doroGameMapper = doroGameMapper;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    public InvitationReadDTO create(@RequestBody InvitationCreateDTO request) {
        UUID userId = currentUserProvider.getCurrentUserId();
        GameConfig config = new GameConfig(
                FieldVariant.fromString(request.fieldVariant()),
                WinCheckerVariant.fromString(request.winCheckerVariant()),
                request.turn()
        );
        return invitationMapper.toDto(
                publicInvitationService.createPublicInvitation(new CreatePublicInvitationCommand(userId, config))
        );
    }

    @GetMapping
    public List<InvitationReadDTO> getAll() {
        return publicInvitationService.getAllPublicInvitations().stream()
                .map(invitationMapper::toDto)
                .toList();
    }

    @PostMapping("/{id}/accept")
    public DoroGameDTO accept(@PathVariable UUID id) {
        UUID userId = currentUserProvider.getCurrentUserId();
        return doroGameMapper.toDto(
                publicInvitationService.acceptPublicInvitation(new AcceptPublicInvitationCommand(id, userId))
        );
    }
}
