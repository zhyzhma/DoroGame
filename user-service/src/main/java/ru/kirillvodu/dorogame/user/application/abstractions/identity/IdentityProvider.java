package ru.kirillvodu.dorogame.user.application.abstractions.identity;

public interface IdentityProvider {
    String createUser(String username, String password);
    void addRole(String keycloakId, String role);
}
