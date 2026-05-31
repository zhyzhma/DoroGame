package ru.kirillvodu.dorogame.user.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kirillvodu.dorogame.user.application.abstractions.identity.IdentityProvider;
import ru.kirillvodu.dorogame.user.application.abstractions.repositories.UserRepository;
import ru.kirillvodu.dorogame.user.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.user.domain.model.User;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IdentityProvider identityProvider;

    public User register(String name, String password) {
        String keycloakId = identityProvider.createUser(name, password);
        identityProvider.addRole(keycloakId, "PLAYER");
        User user = new User(UUID.fromString(keycloakId), name, 0);
        return userRepository.save(user);
    }

    public User getById(UUID keycloakId) {
        return userRepository.getById(keycloakId)
                .orElseThrow(() -> new ObjectNotFoundException(keycloakId, "User"));
    }

    public List<User> getByIds(List<UUID> keycloakIds) {
        return userRepository.getByIds(keycloakIds);
    }
}
