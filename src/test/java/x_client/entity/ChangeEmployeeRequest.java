package x_client.entity;

public record ChangeEmployeeRequest(String lastName, String email, String url, String phone,
                                    boolean isActive) implements Request{
}
