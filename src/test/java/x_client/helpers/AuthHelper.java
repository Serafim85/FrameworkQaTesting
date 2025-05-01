package x_client.helpers;

import base.BaseProperties;
import base.BaseRestAssured;
import x_client.entity.AuthRequest;
import x_client.entity.AuthResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Optional;

public class AuthHelper extends BaseRestAssured {

    private final BaseProperties properties = getProperties();
    private final String login = properties.getProperties().getProperty("login");
    private final String userName;
    private final String password;
    private int statusCode;

    public AuthHelper(String userName, String password) {
        this.userName = userName;
        this.password = password;
        RestAssured.baseURI = getUrl();
    }

    public Optional<String> authorization() {
        AuthRequest authRequest = new AuthRequest(userName, password);

        Response response = createByRequest(login, "", authRequest);
        statusCode = getStatusCode(response);

        if(statusCode == 201) {
            AuthResponse authResponse = createObjectByResponse(response, AuthResponse.class);
            return Optional.of(authResponse.userToken());
        } else
            return Optional.empty();
    }

    public Response getResponse() {
        AuthRequest authRequest = new AuthRequest(userName, password);

        Response response = createByRequest(login, "", authRequest);
        statusCode = getStatusCode(response);

        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
