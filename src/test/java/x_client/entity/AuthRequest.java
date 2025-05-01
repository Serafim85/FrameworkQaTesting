package x_client.entity;

public record AuthRequest(String username, String password) implements Request {

}
