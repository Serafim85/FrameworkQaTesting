package x_client;

import base.BaseRestAssured;
import base.BaseProperties;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.Tag;
import x_client.entity.ChangeEmployeeRequest;
import x_client.entity.CreateEmployeeRequest;
import x_client.entityDb.CompanyEntity;
import x_client.entityDb.EmployeeEntity;
import x_client.helpers.AuthHelper;
import x_client.helpers.EmployeeHelper;
import x_client.helpersDb.CompanyDbHelper;
import x_client.helpersDb.EmployeeDbHelper;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Owner("Валентин Врублевский")
@Epic("X-Clients Сервис записи на прием к профильным специалистам.")
@DisplayName("API Тест для проверки Crud операций с Сотрудниками на соответствие схеме")
public class EmployeeJsonSchemaTest extends BaseRestAssured {

    private static EmployeeHelper employeeHelper;
    private static String token;
    private static int companyId;
    private static int employeeId;
    private static CompanyEntity company;
    private static CompanyDbHelper companyDbHelper;
    private static EmployeeDbHelper employeeDbHelper;
    private static BaseProperties propertiesHelper;

    static RequestSpecification requestSpecification;

    @BeforeAll
    public static void setUp() throws IOException {
        propertiesHelper = new BaseProperties();
        employeeDbHelper = new EmployeeDbHelper();
        companyDbHelper = new CompanyDbHelper();
        RestAssured.baseURI = propertiesHelper.getURL();
        employeeHelper = auth();
        requestSpecification = getRequestSpecification("employee", token);
    }

    private static EmployeeHelper auth() {
        AuthHelper authHelper = new AuthHelper(propertiesHelper.getUserAdmin(), propertiesHelper.getPasswordAdmin());

        token = authHelper.authorization().orElse("");
        company = companyDbHelper.createNewCompany();
        companyId = company.getId();
        EmployeeEntity employee = employeeDbHelper.createNewEmployee(companyId);
        employeeId = employee.getId();
        return new EmployeeHelper(token, companyId);
    }

    @Test
    @DisplayName("Проверить список сотрудников на соответствие схеме")
    @Description("Получаем список сотрудников и проверяем соответствие схеме")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Позитивный")
    @Disabled("Баг. Response Body не соответствует схеме. " +
            "Ответ содержит три лишних поля (createDateTime, lastChangedDateTime и avatar_url) " +
            "String email содержит null, birthdate не соответствует типу.")
    public void checkGetEmployeeListByCompanyId() {
        checkListByParamsInt(requestSpecification, "company", companyId,200);
    }

    @Test
    @DisplayName("Проверить, что id компании в списке сотрудников может быть только number")
    @Description("id компании в списке сотрудников может быть только цифровым")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Негативный")
    @Disabled("Тип id компании не может быть никаким, кроме Integer или int. " +
            "Иначе ответ содержит statusCode 500, который не описан в схеме. ")
    public void checkEmployeeListCompanyIdIsNumber() {
        String companyId = "aa";
        checkListByParamsString(requestSpecification, "company", companyId, 500);
    }

    @Test
    @DisplayName("Проверить, что id компании в списке сотрудников обязательное поле")
    @Description("id компании в списке сотрудников поле обязательное по схеме")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Негативный")
    @Disabled("id компании в списке сотрудников обязательное поле!!!")
    public void checkEmployeeListRequiredCompanyId() {
        checkListByRequiredParams(requestSpecification, 500);
    }

    @Test
    @DisplayName("Проверить создание сотрудника на соответствие схеме")
    @Description("Созданный сотрудник соответствует схеме")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Позитивный")
    public void checkCreateEmployee() {
        CreateEmployeeRequest createEmployeeRequest = employeeHelper.getCreateEmployeeRequest();
        checkCreateObject(requestSpecification, createEmployeeRequest, 201);
    }

    @Test
    @DisplayName("Проверить, получение сотрудника по id соответствует схеме")
    @Description("Проверяем, что полученный сотрудник соответствует схеме")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Позитивный")
    @Disabled("Сотрудник не соответствует схеме")
    public void checkGetEmployeeById() {
        checkGetObjectById(requestSpecification, employeeId, 200);
    }

    @Test
    @DisplayName("Проверить, получение сотрудника по несуществующему id соответствует схеме")
    @Description("Проверяем, что полученный сотрудник по несуществующему id соответствует схеме")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Негативный")
    @Disabled("Баг. Запрос вернул код 200, а по документации должен быть код 404. ")
    public void checkGetEmployeeByIdNotExists() {
        employeeId = -100;
        checkGetObjectIdIsNotExists(requestSpecification, employeeId, 404);
    }

    @Test
    @DisplayName("Проверить, что изменение информации о сотруднике соответствует схеме")
    @Description("Проверить, что изменение информации о сотруднике соответствует схеме")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Позитивный")
    @Disabled("Баг. Запрос вернул код 200, а по документации должен быть код 201." +
            "Запрос содержит 5 полей, а ответ - 4. Поле phone не обрабатывается.")
    public void getChangeEmployeeById() {
        ChangeEmployeeRequest changeEmployeeRequest = employeeHelper.getChangeEmployeeRequest();
        checkChangeObjectById(requestSpecification, changeEmployeeRequest, employeeId);

    }

    @Test
    @DisplayName("Изменение информации о сотруднике. Проверить что token - обязательное поле")
    @Description("Проверяем что token - обязательное поле")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Негативный")
    @Disabled("Token - обязательное поле.")
    public void getChangeEmployeeByIdTokenIsRequired() {
        ChangeEmployeeRequest changeEmployeeRequest = employeeHelper.getChangeEmployeeRequest();
        checkThatTokenIsRequired("employee", changeEmployeeRequest, employeeId);
    }

    @Test
    @DisplayName("Изменение информации о сотруднике. Проверить что id - обязательное поле")
    @Description("Проверить что id - обязательное поле")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Негативный")
    @Disabled("Id - обязательное поле. Patch запрос запрещен.")
    public void getChangeEmployeeByIdIsRequiredId() {
        ChangeEmployeeRequest changeEmployeeRequest = employeeHelper.getChangeEmployeeRequest();
        checkChangeObjectIdIsRequired(requestSpecification, changeEmployeeRequest);
    }

    @Test
    @DisplayName("Изменение информации о сотруднике. Проверить что id - цифровое поле")
    @Description("Проверяем что id - должно быть цифровым полем")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Негативный")
    @Disabled("Id - String не соответствует схеме. Тип id может быть только integer.")
    public void getChangeEmployeeByIdIsStringId() {
        ChangeEmployeeRequest changeEmployeeRequest = employeeHelper.getChangeEmployeeRequest();
        checkChangeObjectIdIsString(requestSpecification, changeEmployeeRequest);
    }

    @Test
    @DisplayName("Изменение информации о сотруднике. Проверить что тело запроса - обязательно")
    @Description("Проверяем, что при изменении информации о сотруднике тело запроса - обязательно")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Негативный")
    @Disabled("Тело запроса - обязательное поле.")
    public void getChangeEmployeeByIdRequestBodyIsRequired() {
        ChangeEmployeeRequest changeEmployeeRequest = employeeHelper.getChangeEmployeeRequest();
        checkChangeObjectRequestBodyIsRequired(requestSpecification, employeeId);
    }

    @AfterAll
    public static void end() {
        companyDbHelper.deleteNewCompany(company);
    }
}
