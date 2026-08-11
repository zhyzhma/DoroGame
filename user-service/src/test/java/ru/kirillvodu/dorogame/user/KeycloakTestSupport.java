package ru.kirillvodu.dorogame.user;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public final class KeycloakTestSupport {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl;
    private final String realm;

    public KeycloakTestSupport(String baseUrl, String realm) {
        this.baseUrl = baseUrl;
        this.realm = realm;
    }

    public String fetchMasterAdminToken() {
        return fetchToken("master", "admin-cli", "admin", "admin");
    }

    public void createRealmUser(String adminToken, String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        Map<String, Object> credential = Map.of(
                "type", "password",
                "value", password,
                "temporary", false
        );
        Map<String, Object> body = Map.of(
                "username", username,
                "enabled", true,
                "credentials", new Object[]{credential}
        );

        restTemplate.postForEntity(
                baseUrl + "/admin/realms/" + realm + "/users",
                new HttpEntity<>(body, headers),
                Void.class
        );
    }

    public String fetchUserToken(String username, String password) {
        return fetchToken(realm, "admin-cli", username, password);
    }

    public String regenerateClientSecret(String adminToken, String clientId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> clients = restTemplate.exchange(
                baseUrl + "/admin/realms/" + realm + "/clients?clientId=" + clientId,
                org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        ).getBody();
        String internalId = (String) clients.get(0).get("id");

        @SuppressWarnings("unchecked")
        Map<String, Object> secretResponse = restTemplate.postForObject(
                baseUrl + "/admin/realms/" + realm + "/clients/" + internalId + "/client-secret",
                new HttpEntity<>(headers),
                Map.class
        );
        return (String) secretResponse.get("value");
    }

    private String fetchToken(String tokenRealm, String clientId, String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        form.add("username", username);
        form.add("password", password);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(
                baseUrl + "/realms/" + tokenRealm + "/protocol/openid-connect/token",
                new HttpEntity<>(form, headers),
                Map.class
        );
        return (String) response.get("access_token");
    }
}
