package x_client.helpers;

import base.BaseProperties;
import base.BaseRestAssured;
import x_client.entity.Company;
import x_client.entity.CreateCompanyRequest;
import x_client.entity.CreateCompanyResponse;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CompanyHelper extends BaseRestAssured {

    private final BaseProperties properties = getProperties();
    private final String company = properties.getProperties().getProperty("company");
    private final String deleteCompany = properties.getProperties().getProperty("deleteCompany");
    private final String URL = getUrl();
    private final String userAdmin = getUserAdmin();
    private final String passwordAdmin = getPasswordAdmin();
    private String userName;
    private String token;
    private int statusCode;

    public CompanyHelper(String userName, String token) {
        this.userName = userName;
        this.token = token;
        RestAssured.baseURI = URL;
    }

    public CompanyHelper() {
        RestAssured.baseURI = URL;
        this.token = "";
    }

    public CompanyHelper(String token) {
        RestAssured.baseURI = URL;
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }
    public String getToken() {
        return token;
    }
    public int getStatusCode() { return statusCode; }
    public void setToken(String token) {
        this.token = token;
    }

    public List<Company> getCompanyList() throws IOException {
        Response response = getResponse(company, token);
        statusCode = getStatusCode(response);
        System.out.println(statusCode + " Список компаний получен");

        return List.of(createObjectByResponse(response, Company[].class));
    }

    public CreateCompanyResponse createCompany(String name, String description) {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest(name, description);

        Response response = createByRequest(company, token, createCompanyRequest);
        statusCode = getStatusCode(response);

        if (statusCode == 201) {
            CreateCompanyResponse createCompanyResponse = createObjectByResponse(response, CreateCompanyResponse.class);
            System.out.println(statusCode + " Компания создана с id = " + createCompanyResponse.id());

            return createCompanyResponse;
        } else {
            System.out.println(statusCode + " У пользователя нет прав создавать новые компании");

            return new CreateCompanyResponse(0);
        }
    }

    public Optional<Company> getCompanyById(int id) {
        Response response = getResponseById(company, token, id);

        statusCode = getStatusCode(response);
        String header = getHeaderLength(response);

        if (header != null && header.equals("0")) {
            System.out.println(statusCode + " Компания с id = " + id + " не найдена");

            return Optional.empty();
        } else {
            System.out.println(statusCode + " Компания с id = " + id + " найдена");

            return Optional.of(createObjectByResponse(response, Company.class));
        }
    }

    public int deleteCompanyById(int id) {
        Response response = deleteById(deleteCompany, token, id);
        int statusCode = getStatusCode(response);

        if (statusCode == 200) {
            System.out.println(statusCode + " Компания с id = " + id + " удалена");

            return id;
        } else {
            System.out.println(statusCode + " У пользователя нет прав удалять компании");

            return 0;
        }
    }

    public int createCompanyAsAdmin(String name, String description) {
        AuthHelper authHelper = new AuthHelper(userAdmin, passwordAdmin);
        String tokenAdmin = authHelper.authorization().orElse("");
        setToken(tokenAdmin);

        return createCompany(name, description).id();
    }

    public int deleteCompanyAsAdmin(int id) {
        AuthHelper authHelper = new AuthHelper(userAdmin, passwordAdmin);
        String tokenAdmin = authHelper.authorization().orElse("");
        setToken(tokenAdmin);

        return deleteCompanyById(id);
    }
}
