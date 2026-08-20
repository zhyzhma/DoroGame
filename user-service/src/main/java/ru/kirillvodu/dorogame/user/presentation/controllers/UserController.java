package ru.kirillvodu.dorogame.user.presentation.controllers;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kirillvodu.dorogame.user.application.abstractions.repositories.AvatarContent;
import ru.kirillvodu.dorogame.user.application.abstractions.security.CurrentUserProvider;
import ru.kirillvodu.dorogame.user.application.contracts.DTO.create.UserCreateDTO;
import ru.kirillvodu.dorogame.user.application.contracts.DTO.read.UserReadDTO;
import ru.kirillvodu.dorogame.user.application.exceptions.AvatarUploadException;
import ru.kirillvodu.dorogame.user.application.mappers.UserMapper;
import ru.kirillvodu.dorogame.user.application.services.UserService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final CurrentUserProvider currentUserProvider;

    public UserController(UserService userService,
                          UserMapper userMapper,
                          CurrentUserProvider currentUserProvider) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping("/register")
    public UserReadDTO register(@RequestBody UserCreateDTO request) {
        return userMapper.toDto(userService.register(request.name(), request.password()));
    }

    @PostMapping(value = "/upload_avatar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserReadDTO uploadAvatar(@PathVariable UUID id, @RequestPart("file") MultipartFile file) {
        try {
            return userMapper.toDto(userService.uploadAvatar(id,
                    file.getInputStream(),
                    file.getSize(),
                    file.getContentType()));
        } catch (IOException e) {
            throw new AvatarUploadException(e.getMessage());
        }
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<InputStreamResource> downloadAvatar(@PathVariable UUID id) {
        AvatarContent avatar = userService.downloadAvatar(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.contentType()))
                .contentLength(avatar.size())
                .body(new InputStreamResource(avatar.stream()));
    }

    @GetMapping("/me")
    public UserReadDTO getMe() {
        return userMapper.toDto(userService.getById(currentUserProvider.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public UserReadDTO getById(@PathVariable UUID id) {
        return userMapper.toDto(userService.getById(id));
    }
}
