package ru.kirillvodu.dorogame.user.infrastructure.security;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.user.application.abstractions.identity.IdentityProvider;

import jakarta.ws.rs.core.Response;
import java.util.List;

@Component
public class KeycloakIdentityUserProvider implements IdentityProvider {

    private final Keycloak keycloak;

    @Value("${keycloak.admin.realm}")
    private String realm;

    public KeycloakIdentityUserProvider(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public String createUser(String username, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(List.of(credential));

        Response response = keycloak.realm(realm).users().create(user);
        return CreatedResponseUtil.getCreatedId(response);
    }

    @Override
    public void addRole(String keycloakId, String role) {
        RoleRepresentation roleRep = keycloak.realm(realm).roles().get(role).toRepresentation();
        keycloak.realm(realm).users().get(keycloakId).roles().realmLevel().add(List.of(roleRep));
    }
}
