package ru.kirillvodu.dorogame.user.presentation.controllers;

import org.springframework.web.bind.annotation.*;
import ru.kirillvodu.dorogame.user.application.abstractions.security.CurrentUserProvider;
import ru.kirillvodu.dorogame.user.application.contracts.DTO.create.UserCreateDTO;
import ru.kirillvodu.dorogame.user.application.contracts.DTO.read.UserReadDTO;
import ru.kirillvodu.dorogame.user.application.mappers.UserMapper;
import ru.kirillvodu.dorogame.user.application.services.UserService;

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

    @GetMapping("/me")
    public UserReadDTO getMe() {
        return userMapper.toDto(userService.getById(currentUserProvider.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public UserReadDTO getById(@PathVariable UUID id) {
        return userMapper.toDto(userService.getById(id));
    }
}
