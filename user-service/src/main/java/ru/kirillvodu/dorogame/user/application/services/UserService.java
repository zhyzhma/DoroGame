package ru.kirillvodu.dorogame.user.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kirillvodu.dorogame.user.application.abstractions.identity.IdentityProvider;
import ru.kirillvodu.dorogame.user.application.abstractions.repositories.AvatarContent;
import ru.kirillvodu.dorogame.user.application.abstractions.repositories.AvatarStorage;
import ru.kirillvodu.dorogame.user.application.abstractions.repositories.UserRepository;
import ru.kirillvodu.dorogame.user.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.user.domain.model.User;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IdentityProvider identityProvider;
    @Autowired
    private AvatarStorage avatarStorage;

    public User register(String name, String password) {
        String keycloakId = identityProvider.createUser(name, password);
        identityProvider.addRole(keycloakId, "PLAYER");
        User user = new User(UUID.fromString(keycloakId), name);
        return userRepository.save(user);
    }

    public User uploadAvatar(UUID id, InputStream in, long size, String contentType) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, "User"));
        String key = avatarStorage.uploadAvatar(in, size, contentType);
        user.setAvatarKey(key);
        return userRepository.save(user);
    }

    public AvatarContent downloadAvatar(UUID id) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, "User"));
        if (user.getAvatarKey() == null) {
            throw new ObjectNotFoundException(id, "Avatar");
        }
        return avatarStorage.downloadAvatar(user.getAvatarKey())
                .orElseThrow(() -> new ObjectNotFoundException(id, "Avatar"));
    }

    public User getById(UUID keycloakId) {
        return userRepository.getById(keycloakId)
                .orElseThrow(() -> new ObjectNotFoundException(keycloakId, "User"));
    }

    public List<User> getByIds(List<UUID> keycloakIds) {
        return userRepository.getByIds(keycloakIds);
    }
}
