package x_client;

import base.BaseProperties;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import x_client.entity.Company;
import x_client.entity.CreateCompanyResponse;
import x_client.helpers.CompanyHelper;
import x_client.helpers.servise.AuthUserNameAndPassword;
import x_client.resolvers.CompanyResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Owner("Валентин Врублевский")
@Epic("X-Clients Сервис записи на прием к профильным специалистам.")
@DisplayName("API Тест для проверки Crud операций с Компаниями")
@ExtendWith({CompanyResolver.class})
class CompanyContractTest {

    private static String name;
    private static String description;

    @BeforeAll
    public static void setUp() throws IOException {
        BaseProperties properties = new BaseProperties();
        name = properties.getName();
        description = properties.getDescription();
    }

    @Test
    @DisplayName("Получить список компаний")
    @Description("Получаем список внесенных компаний")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void getCompanyList(@AuthUserNameAndPassword(userName = "", password = "") CompanyHelper companyHelper) throws IOException {
        companyHelper.getCompanyList();
    }

    @Test
    @DisplayName("Создать компанию возвращает код 201")
    @Description("Создание новой компании")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void createCompany(@AuthUserNameAndPassword(userName = "leonardo", password = "leads") CompanyHelper companyHelper) {
        CreateCompanyResponse createCompanyResponse = companyHelper.createCompany(name, description);

        companyHelper.deleteCompanyById(createCompanyResponse.id());
    }

    @Test
    @DisplayName("Получить компанию  по id ")
    @Description("Найти компанию по заданному id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void getCompanyById(@AuthUserNameAndPassword(userName = "leonardo", password = "leads") CompanyHelper companyHelper) {
        int id = companyHelper.createCompany(name, description).id();
        Optional<Company> company = companyHelper.getCompanyById(id);

        if(company.isPresent()) {
            assertThat(company.get().id()).isEqualTo(id);
            companyHelper.deleteCompanyById(id);
        }
        else
            System.out.println("Компания не существует");
    }

    @Test
    @DisplayName("Удалить компанию возвращает код 200 ")
    @Description("Удалить компанию по заданному id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void deleteCompanyById(@AuthUserNameAndPassword(userName = "leonardo", password = "leads") CompanyHelper companyHelper) {
        int id = companyHelper.createCompany(name, description).id();

        assertThat(companyHelper.deleteCompanyById(id)).isEqualTo(id);
    }

    @Test
    @DisplayName("Клиент. Создать компанию возвращает код 403")
    @Description("Клиент. Создает новую компанию")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    void createCompanyClient(@AuthUserNameAndPassword(userName = "stella", password = "sun-fairy") CompanyHelper companyHelper) {
        CreateCompanyResponse createCompanyResponse = companyHelper.createCompany(name, description);

        assertThat(createCompanyResponse.id()).isZero();
        companyHelper.deleteCompanyById(createCompanyResponse.id());
    }

    @Test
    @DisplayName("Клиент. Удалить компанию возвращает код 403")
    @Description("Клиент. Удаляет компанию компанию по id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    void deleteCompanyByIdClient(@AuthUserNameAndPassword(userName = "stella", password = "sun-fairy") CompanyHelper companyHelper) {
        String token = companyHelper.getToken();
        int id = companyHelper.createCompanyAsAdmin(name, description);
        companyHelper.setToken(token);

        assertThat(companyHelper.deleteCompanyAsAdmin(id)).isEqualTo(id);
    }

    @Test
    @DisplayName("Клиент. Получить компанию  по id возвращает код 200")
    @Description("Клиент. Находит  компанию по заданному id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void getCompanyByIdClient(@AuthUserNameAndPassword(userName = "stella", password = "sun-fairy") CompanyHelper companyHelper) {
        int id = companyHelper.createCompanyAsAdmin(name, description);

        Optional<Company> company = companyHelper.getCompanyById(id);

        if(company.isPresent()) {
            assertThat(company.get().id()).isEqualTo(id);
            companyHelper.deleteCompanyById(id);
        }
        else
            System.out.println("Компания не существует");
    }

    @Test
    @DisplayName("Гость. Создать компанию возвращает код 401")
    @Description("Гость. Создает новую компанию")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    void createCompanyGuest(@AuthUserNameAndPassword(userName = "", password = "") CompanyHelper companyHelper) {
        CreateCompanyResponse createCompanyResponse = companyHelper.createCompany(name, description);

        assertThat(createCompanyResponse.id()).isZero();

        companyHelper.deleteCompanyById(createCompanyResponse.id());
    }

    @Test
    @DisplayName("Гость. Удалить компанию возвращает код 401")
    @Description("Гость. Удаляет компанию компанию по id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    void deleteCompanyByIdGuest(@AuthUserNameAndPassword(userName = "", password = "") CompanyHelper companyHelper) {
        String token = companyHelper.getToken();
        int id = companyHelper.createCompanyAsAdmin(name, description);
        companyHelper.setToken(token);

        assertThat(companyHelper.deleteCompanyAsAdmin(id)).isEqualTo(id);
    }

    @Test
    @DisplayName("Гость. Получить компанию  по id возвращает код 200")
    @Description("Гость. Находит  компанию по заданному id")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void getCompanyByIdGuest(@AuthUserNameAndPassword(userName = "", password = "") CompanyHelper companyHelper) {
        int id = companyHelper.createCompanyAsAdmin(name, description);

        Optional<Company> company = companyHelper.getCompanyById(id);

        if(company.isPresent()) {
            assertThat(company.get().id()).isEqualTo(id);
            companyHelper.deleteCompanyById(id);
        }
        else
            System.out.println("Компания не существует");
    }

    @AfterAll
    public static void deleteAllCompaniesByName(@AuthUserNameAndPassword(userName = "leonardo", password = "leads") CompanyHelper companyHelper) throws IOException {
        List<Company> companyList = companyHelper.getCompanyList();
        for (Company company : companyList) {
            if(company.name().equals(name)) {
                companyHelper.deleteCompanyById(company.id());
            }
        }
    }
}
