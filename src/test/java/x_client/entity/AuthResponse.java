package x_client.entity;

public record AuthResponse(String userToken, String role, String displayName, String login) {

}
