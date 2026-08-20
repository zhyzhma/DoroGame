package ru.kirillvodu.dorogame.user.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;
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

    @Column(name = "avatar_key")
    private String avatarKey;
}
