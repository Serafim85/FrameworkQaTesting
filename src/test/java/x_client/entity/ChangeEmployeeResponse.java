package x_client.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChangeEmployeeResponse(int id, String lastName, String email, String url, boolean isActive) {
}
