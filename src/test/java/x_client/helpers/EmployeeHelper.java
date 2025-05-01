package x_client.helpers;

import base.BaseRestAssured;
import base.BaseProperties;
import x_client.entity.ChangeEmployeeRequest;
import x_client.entity.ChangeEmployeeResponse;
import x_client.entity.CreateEmployeeRequest;
import x_client.entity.CreateEmployeeResponse;
import x_client.entity.Employee;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.instancio.Instancio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class EmployeeHelper extends BaseRestAssured {

    private String token;
    private final int companyId;
    private final int size;
    private int statusCode;
    private final BaseProperties properties = getProperties();
    private final String employee = properties.getProperties().getProperty("employee");

    public EmployeeHelper(String token, int companyId, int size) {
        this.token = token;
        this.companyId = companyId;
        this.size = size;
        RestAssured.baseURI = properties.getURL();
    }

    public EmployeeHelper(String token, int companyId) {
        this.token = token;
        this.companyId = companyId;
        this.size = 0;
        RestAssured.baseURI = properties.getURL();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Employee> getEmployeeList(int companyId) {
        System.out.println("Получить список сотрудников");
        createEmployeeList();
        Response response = getResponseByParam(employee, token,"company", companyId);

        System.out.println(getStatusCode(response) + " Список сотрудников компании с id = " + companyId + " получен.");

        return List.of(createObjectByResponse(response, Employee[].class));
    }

    public Optional<Employee> getEmployeeById(int id) {
        System.out.println("Получить сотрудника по id = " + id);

        Response response = getResponseById(employee, token, id);
        statusCode = getStatusCode(response);
        String contentLength = getHeaderByParam(response,"Content-Length");

        if (contentLength != null && contentLength.equals("0")) {
            System.out.println(statusCode + " Сотрудник с id = " + id + " не найден");

            return Optional.empty();

        } else {
            System.out.println(statusCode + " Сотрудник с id = " + id + " найден");

            return Optional.of(createObjectByResponse(response, Employee.class));
        }
    }

    public CreateEmployeeResponse createEmployee() {
        CreateEmployeeRequest createEmployeeRequest = getCreateEmployeeRequest();

        Response response = createByRequest(employee, token, createEmployeeRequest);
        statusCode = getStatusCode(response);

        if (statusCode == 201) {
            CreateEmployeeResponse createEmployeeResponse = response.as(CreateEmployeeResponse.class);
            System.out.println(statusCode + " Создан сотрудник с id " + createEmployeeResponse.id());

            return createEmployeeResponse;
        } else {
            System.out.println(statusCode + " У пользователя нет прав добавлять новых сотрудников");

            return new CreateEmployeeResponse(0);
        }
    }

    public CreateEmployeeRequest getCreateEmployeeRequest() {
        Employee employee = Instancio.create(Employee.class);
        LocalDateTime localDateTime = LocalDateTime.now().minusYears(20);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String birthDate = localDateTime.format(formatter);

        return new CreateEmployeeRequest(0, employee.getFirstName(),
                employee.getLastName(), employee.getMiddleName(), companyId,
                "charon85@mail.ru",
                employee.getAvatar_url(),
                employee.getPhone(), birthDate,
                employee.getIsActive());
    }

    public ChangeEmployeeResponse changeEmployeeById(int id) {
        ChangeEmployeeRequest changeEmployeeRequest = getChangeEmployeeRequest();

        Response response = changeObjectById(employee, token, changeEmployeeRequest, id);
        statusCode = getStatusCode(response);

        if (statusCode == 200) {
            System.out.println(statusCode + " Информацию о сотруднике изменена");

            return createObjectByResponse(response, ChangeEmployeeResponse.class);
        } else {
            System.out.println(statusCode + " У пользователя нет прав изменять информацию о сотрудниках");

            new ChangeEmployeeResponse(0, "", "", "", false);
        }
        return null;
    }

    public void createEmployeeList() {
        for (int i = 0; i < size; i++) {
            createEmployee();
        }
    }

    public ChangeEmployeeRequest getChangeEmployeeRequest() {
        Employee employee = Instancio.create(Employee.class);

        return new ChangeEmployeeRequest(employee.getLastName(),
                "valentinvrublevskaja@yandex.ru", employee.getAvatar_url(),
                employee.getPhone(),
                employee.getIsActive());
    }

    public String authAs(String name, String password) {

        AuthHelper authHelper = new AuthHelper(name, password);
        String token = authHelper.authorization().orElse("");
        setToken(token);

        return token;
    }
}
