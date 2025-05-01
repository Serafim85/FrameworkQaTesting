package base;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import x_client.entity.Request;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public abstract class BaseRestAssured {
    private static BaseProperties properties;
    private static String header;

    {
        try {
            properties = new BaseProperties();
            header = properties.getHeader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BaseProperties getProperties() {
        return properties;
    }

    public String getUrl() {
        return properties.getURL();
    }

    public String getUserAdmin() {
        return properties.getUserAdmin();
    }

    public String getPasswordAdmin() {
        return properties.getPasswordAdmin();
    }

    public Response getResponse(String basePath, String token) {

        return given().filter(new AllureRestAssured())
                .header(header, token)
                .basePath(basePath)
                .when()
                .get();
    }

    public Response createByRequest(String basePath, String token, Request request) {

        return given().filter(new AllureRestAssured())
                .header(header, token)
                .basePath(basePath)
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post();
    }

    public Response getResponseById(String basePath, String token, int id) {

        return given().filter(new AllureRestAssured())
                .header(header, token)
                .basePath(basePath)
                .when()
                .get("{id}", id);
    }

    public Response getResponseByParam(String basePath, String token, String paramName, int paramValue) {

        return given().filter(new AllureRestAssured()).queryParam(paramName, paramValue)
                .header(header, token)
                .basePath(basePath)
                .when()
                .get();
    }

    public Response deleteById(String basePath, String token, int id) {

        return given().filter(new AllureRestAssured())
                .header(header, token)
                .basePath(basePath)
                .when()
                .get("{id}", id);
    }

    public Response changeObjectById(String basePath, String token, Request request, int id) {

        return given().filter(new AllureRestAssured())
                .header(header, token)
                .basePath(basePath)
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .patch("{id}", id);
    }

    public String getHeaderLength(Response response) {
        return response.header("Content-Length");
    }

    public String getHeaderByParam(Response response, String headerParam) {
        return response.header(headerParam);
    }

    public int getStatusCode(Response response) {
        return response.statusCode();
    }

    public <T> T createObjectByResponse(Response response, Class<T> clazz) {
        return response.as(clazz);
    }

    public static RequestSpecification getRequestSpecification(String basePath, String token) {

        return given()
                .header(header, token)
                .filters(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter(),
                        new OpenApiValidationFilter(properties.getSwaggerURL()),
                        new AllureRestAssured()
                )
                .basePath(basePath)
                .contentType(ContentType.JSON);
    }

    public void checkListByParamsInt(RequestSpecification requestSpecification, String paramName,
                                     int paramValue, int statusCode) {
        given(requestSpecification)
                .queryParam(paramName, paramValue)
                .when()
                .get()
                .then()
                .statusCode(statusCode);
    }

    public void checkListByParamsString(RequestSpecification requestSpecification, String paramName,
                                        String paramValue, int statusCode) {
        given(requestSpecification)
                .queryParam(paramName, paramValue)
                .when()
                .get()
                .then()
                .statusCode(statusCode);
    }

    public void checkListByRequiredParams(RequestSpecification requestSpecification, int statusCode) {
        given(requestSpecification)
                .when()
                .get()
                .then()
                .statusCode(statusCode);
    }

    public void checkCreateObject(RequestSpecification requestSpecification, Request request, int statusCode) {
        given(requestSpecification)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(statusCode);
    }

    public void checkGetObjectById(RequestSpecification requestSpecification, int id, int statusCode) {
        given(requestSpecification)
                .when()
                .get("{id}", id)
                .then()
                .statusCode(statusCode);
    }

    public void checkGetObjectIdIsNotExists(RequestSpecification requestSpecification, int id, int statusCode) {
        given(requestSpecification)
                .when()
                .get("{id}", id)
                .then()
                .statusCode(statusCode);
    }

    public void checkChangeObjectById(RequestSpecification requestSpecification, Request request, int id) {
        given(requestSpecification)
                .body(request)
                .when()
                .patch("{id}", id);
    }

    public void checkThatTokenIsRequired(String basePath, Request request, int id) {
        given()
                .filters(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter(),
                        new OpenApiValidationFilter(properties.getSwaggerURL()),
                        new AllureRestAssured()
                )
                .basePath(basePath)
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .patch("{id}", id);
    }

    public void checkChangeObjectIdIsRequired(RequestSpecification requestSpecification, Request request) {
        given(requestSpecification)
                .body(request)
                .when()
                .patch();
    }

    public void checkChangeObjectIdIsString(RequestSpecification requestSpecification, Request request) {
        given(requestSpecification)
                .body(request)
                .when()
                .patch("{id}", "employeeId");
    }

    public void checkChangeObjectRequestBodyIsRequired(RequestSpecification requestSpecification, int id) {
        given(requestSpecification)
                .when()
                .patch("{id}", id);
    }
}
