package x_client;

import base.BaseProperties;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.Tag;
import x_client.entity.ChangeEmployeeResponse;
import x_client.entity.CreateEmployeeResponse;
import x_client.entity.Employee;
import x_client.entityDb.CompanyEntity;
import x_client.entityDb.EmployeeEntity;
import x_client.helpers.EmployeeHelper;
import x_client.helpersDb.CompanyDbHelper;
import x_client.helpersDb.EmployeeDbHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Owner("Валентин Врублевский")
@Epic("X-Clients Сервис записи на прием к профильным специалистам.")
@DisplayName("API Бизнес тесты проверки работы сервиса")
public class EmployeeBusinessTest {

    private static int companyId;
    private static CompanyEntity company;
    private static CompanyDbHelper companyDbHelper;
    private static EmployeeDbHelper employeeDbHelper;
    private static EmployeeHelper employeeHelper;
    static String token;

    @BeforeAll
    public static void setUp() throws IOException {
        BaseProperties propertiesHelper = new BaseProperties();
        employeeDbHelper = new EmployeeDbHelper();
        companyDbHelper = new CompanyDbHelper();

        token = propertiesHelper.getTokenAdmin();
        company = companyDbHelper.createNewCompany();
        companyId = company.getId();
    }

    @Test
    @DisplayName("Добавить нового сотрудника. Проверить, что сотрудник добавлен")
    @Description("Проверяем, что добавленный сотрудник находится в базе данных")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void createEmployee() {
        employeeHelper = new EmployeeHelper(token, companyId);

        CreateEmployeeResponse newEmployee = employeeHelper.createEmployee();
        EmployeeEntity employee = employeeDbHelper.getEmployeeFromDB(newEmployee.id());

        assertThat(newEmployee.id()).isEqualTo(employee.getId());
    }

    @Test
    @DisplayName("Получить список сотрудников компании, увеличенный на size человек")
    @Description("Проверяем, что список сотрудников увеличился на size человек")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void getEmployeeList() {
        employeeHelper = new EmployeeHelper(token, companyId, 3);

        int sizeOfEmployeeList = employeeDbHelper.getEmployeeFromDBByCompanyId(companyId).size();
        List<Employee> employeeList = employeeHelper.getEmployeeList(companyId);

        assertThat(employeeList).hasSize(sizeOfEmployeeList+3);
    }

    @Test
    @DisplayName("Изменить информацию о сотруднике по id")
    @Description("Проверяем, что информацию о сотруднике по id изменилась в базе данных")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void changeEmployeeById() {
        employeeHelper = new EmployeeHelper(token, companyId);

        CreateEmployeeResponse employeeResponse = employeeHelper.createEmployee();
        int employeeId = employeeResponse.id();
        Employee employee = employeeHelper.getEmployeeById(employeeId).orElse(new Employee());
        ChangeEmployeeResponse changedEmployee = employeeHelper.changeEmployeeById(employeeId);
        EmployeeEntity changedEntity = employeeDbHelper.getEmployeeFromDB(employeeId);

        assertThat(employee.getEmail()).isNull();
        assertThat(changedEntity.getEmail()).isEqualTo("valentinvrublevskaja@yandex.ru");
        assertThat(changedEmployee.email()).isEqualTo("valentinvrublevskaja@yandex.ru");
    }

    @AfterAll
    public static void end() {
        companyDbHelper.deleteNewCompany(company);
    }
}
