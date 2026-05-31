package ru.kirillvodu.dorogame.user.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.kirillvodu.dorogame.user.domain.model.User;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {

    @Column(name = "keycloak_id", nullable = false, unique = true)
    private UUID keycloakId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "score", nullable = false)
    private int score;

    public User toDomain() {
        return new User(keycloakId, name, score);
    }

    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .keycloakId(user.getId())
                .name(user.getName())
                .score(user.getScore())
                .build();
    }
}
