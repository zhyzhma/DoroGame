package ru.kirillvodu.dorogame.user.application.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.user.application.contracts.DTO.read.UserReadDTO;
import ru.kirillvodu.dorogame.user.domain.model.User;

@Component
public class UserMapper {

    public UserReadDTO toDto(User user) {
        return new UserReadDTO(user.getId(), user.getName(), user.getScore());
    }
}
