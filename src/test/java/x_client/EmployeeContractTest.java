package x_client;

import base.BaseProperties;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.Tag;
import x_client.entity.CreateEmployeeResponse;
import x_client.entityDb.CompanyEntity;
import x_client.helpers.EmployeeHelper;
import x_client.helpersDb.CompanyDbHelper;
import x_client.helpersDb.EmployeeDbHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Owner("Валентин Врублевский")
@Epic("X-Clients Сервис записи на прием к профильным специалистам.")
@DisplayName("API Тест для проверки Crud операций с Сотрудниками")
public class EmployeeContractTest {

    private static int companyId;
    private static CompanyEntity company;
    private static CompanyDbHelper companyDbHelper;
    private static EmployeeDbHelper employeeDbHelper;
    private static EmployeeHelper employeeHelper;
    private static String tokenAdmin;
    private static String tokenClient;

    @BeforeAll
    public static void setUp() throws IOException {
        BaseProperties properties = new BaseProperties();
        employeeDbHelper = new EmployeeDbHelper();
        companyDbHelper = new CompanyDbHelper();

        company = companyDbHelper.createNewCompany();
        companyId = company.getId();
        tokenAdmin = properties.getTokenAdmin();
        tokenClient = properties.getTokenClient();
    }

    @Test
    @DisplayName("Добавить нового сотрудника возвращает код 201")
    @Description("Добавляем нового сотрудника")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void createEmployee() {
        employeeHelper = new EmployeeHelper(tokenAdmin, companyId);
        CreateEmployeeResponse createEmployeeResponse = employeeHelper.createEmployee();

        assertThat(createEmployeeResponse.id()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Получить список сотрудников компании возвращает код 200")
    @Description("Получаем нового сотрудника")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void getEmployeeList() {
        employeeHelper = new EmployeeHelper(tokenAdmin, companyId, 3);
        int sizeOfEmployeeList = employeeDbHelper.getEmployeeFromDBByCompanyId(companyId).size();

        assertThat(employeeHelper.getEmployeeList(companyId).size()).isEqualTo(sizeOfEmployeeList+3);

    }

    @Test
    @DisplayName("Получить сотрудника по id возвращает код 200")
    @Description("Получаем сотрудника по id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void getEmployeeById() {
        employeeHelper = new EmployeeHelper(tokenAdmin, companyId);

        int employeeId = employeeHelper.createEmployee().id();

        employeeHelper.getEmployeeById(employeeId);
    }

    @Test
    @DisplayName("Изменить информацию о сотруднике по id возвращает код 201")
    @Description("Получаем нового сотрудника")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    @Disabled("Баг:  По документации метод должен выдать генерировать код 201, а генерирует код 200" +
            " Изменение поля phone не происходит")
    void changeEmployeeById() {
        employeeHelper = new EmployeeHelper(tokenAdmin, companyId);

        int employeeId = employeeHelper.createEmployee().id();
        employeeHelper.changeEmployeeById(employeeId);
    }

    @Test
    @DisplayName("Клиент. Добавить нового сотрудника возвращает код 201")
    @Description("Клиент. Добавляем нового сотрудника")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void createEmployeeClient() {
        employeeHelper = new EmployeeHelper(tokenClient, companyId);

        employeeHelper.createEmployee().id();

    }

    @Test
    @DisplayName("Клиент. Получить список сотрудников компании возвращает код 200")
    @Description("Клиент. Получаем список сотрудников")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void getEmployeeListClient() {
        employeeHelper = new EmployeeHelper(tokenClient, companyId, 3);

        employeeHelper.getEmployeeList(companyId);
    }

    @Test
    @DisplayName("Клиент. Получить сотрудника по id возвращает код 200")
    @Description("Клиент. Получаем сотрудника по id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void getEmployeeByIdClient() {
        employeeHelper = new EmployeeHelper(tokenClient, companyId);
        int employeeId = employeeHelper.createEmployee().id();

        employeeHelper.getEmployeeById(employeeId);
    }

    @Test
    @DisplayName("Клиент. Изменить сотрудника по id возвращает код 200")
    @Description("Клиент. Изменить информацию о сотруднике по id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    @Disabled("Баг:  По документации метод должен выдать генерировать код 201, а генерирует код 200")
    void changeEmployeeByIdClient() {
        employeeHelper = new EmployeeHelper(tokenClient, companyId);

        int employeeId = employeeHelper.createEmployee().id();

        employeeHelper.changeEmployeeById(employeeId);
    }

    @Test
    @DisplayName("Гость. Добавить нового сотрудника возвращает код 401")
    @Description("Гость. Добавляем нового сотрудника")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    void createEmployeeGuest() {
        employeeHelper = new EmployeeHelper("", companyId);

        employeeHelper.createEmployee();
    }

    @Test
    @DisplayName("Гость. Получить список сотрудников компании возвращает код 200")
    @Description("Гость. Добавляем нового сотрудника")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void getEmployeeListGuest() {
        employeeHelper = new EmployeeHelper("", companyId);

        employeeHelper.getEmployeeList(companyId);
    }

    @Test
    @DisplayName("Гость. Получить сотрудника по id возвращает код 200")
    @Description("Гость. Получаем сотрудника по id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void getEmployeeByIdGuest() {
        employeeHelper = new EmployeeHelper(tokenAdmin, companyId);

        int employeeId = employeeHelper.createEmployee().id();
        employeeHelper.authAs("","");

        employeeHelper.getEmployeeById(employeeId);
    }

    @Test
    @DisplayName("Гость. Изменить информацию о сотруднике по id возвращает код 401")
    @Description("Гость. Изменяем информацию о сотруднике по id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    @Disabled("Баг:  При изменении сотрудника незарегистрированным пользователем генерируется код 401." +
            " В документации этот вариант не описан")
    void changeEmployeeByIdGuest() {
        employeeHelper = new EmployeeHelper(tokenAdmin, companyId);

        int employeeId = employeeHelper.createEmployee().id();
        employeeHelper.authAs("","");

        employeeHelper.changeEmployeeById(employeeId);
    }

    @Test
    @DisplayName("Получить сотрудника по несуществующему id")
    @Description("Получаем сотрудника по несуществующему id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    @Disabled("Баг - поиск сотрудника по несуществующему id возвращает код 200 вместо 404 по документации")
    void getEmployeeByIdNotExist() {
        employeeHelper = new EmployeeHelper(tokenAdmin, companyId);

        int employeeId = -5;

        employeeHelper.getEmployeeById(employeeId);
    }

    @Test
    @DisplayName("Изменить информацию о сотруднике по несуществующему id")
    @Description("Изменяем информацию о сотруднике по несуществующему id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    @Disabled("Баг - поиск сотрудника по несуществующему id возвращает код 500 вместо 404 по документации")
    void changeEmployeeByIdNotExist() {
        employeeHelper = new EmployeeHelper(tokenAdmin, companyId);

        int employeeId = -5;

        employeeHelper.changeEmployeeById(employeeId);
    }

    @AfterAll
    public static void end() {
//        companyDbHelper.deleteNewCompany(company);
        companyDbHelper.deleteAllCompanyByName();
    }
}
