package ru.kirillvodu.dorogame.user.application.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kirillvodu.dorogame.user.application.abstractions.identity.IdentityProvider;
import ru.kirillvodu.dorogame.user.application.abstractions.repositories.UserRepository;
import ru.kirillvodu.dorogame.user.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private IdentityProvider identityProvider;

    @InjectMocks private UserService userService;

    @Test
    void register_savesUserWithZeroScore() {
        UUID keycloakId = UUID.randomUUID();
        when(identityProvider.createUser("Alice", "password")).thenReturn(keycloakId.toString());
        User saved = new User(keycloakId, "Alice", 0);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.register("Alice", "password");

        assertThat(result.getName()).isEqualTo("Alice");
        assertThat(result.getScore()).isEqualTo(0);
        verify(identityProvider).addRole(keycloakId.toString(), "PLAYER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getById_found_returnsUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Bob", 100);
        when(userRepository.getById(id)).thenReturn(Optional.of(user));

        User result = userService.getById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Bob");
    }

    @Test
    void getById_notFound_throwsObjectNotFoundException() {
        UUID id = UUID.randomUUID();
        when(userRepository.getById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.getById(id));
    }

    @Test
    void getByIds_returnsAllMatchingUsers() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        List<UUID> ids = List.of(id1, id2);
        List<User> users = List.of(new User(id1, "A", 0), new User(id2, "B", 0));
        when(userRepository.getByIds(ids)).thenReturn(users);

        List<User> result = userService.getByIds(ids);

        assertThat(result).hasSize(2);
    }
}
