package x_client;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import x_client.entity.AuthResponse;
import x_client.helpers.AuthHelper;
import x_client.helpers.servise.AuthUserNameAndPassword;
import x_client.resolvers.AuthResolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Owner("Валентин Врублевский")
@Epic("X-Clients Сервис записи на прием к профильным специалистам.")
@DisplayName("API Тест для проверки авторизации")
@ExtendWith(AuthResolver.class)
class AuthContractTest {

    @Test
    @DisplayName("Проверка авторизации админа возвращает код 201 и token not null")
    @Description("Проверяем авторизацию админа")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void authorizationAdmin(@AuthUserNameAndPassword(userName = "leonardo", password = "leads") AuthHelper authHelper) {
        System.out.println("Авторизация админа");
        AuthResponse authResponse = authHelper.createObjectByResponse(authHelper.getResponse(), AuthResponse.class);
        assertThat(authResponse.userToken()).isNotEmpty();
        assertEquals(201, authHelper.getStatusCode());
    }

    @Test
    @DisplayName("Проверка авторизации клиента возвращает код 201 и token not null")
    @Description("Проверяем авторизацию клиента")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    void authorizationClient(@AuthUserNameAndPassword(userName = "stella", password = "sun-fairy") AuthHelper authHelper) throws IOException {
        System.out.println("Авторизация клиента");
        AuthResponse authResponse = authHelper.createObjectByResponse(authHelper.getResponse(), AuthResponse.class);
        assertThat(authResponse.userToken()).isNotEmpty();
        assertEquals(201, authHelper.getStatusCode());
    }

    @Test
    @DisplayName("Проверка авторизации гостя возвращает код 401")
    @Description("Проверяем авторизацию гостя")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    void authorizationGuest(@AuthUserNameAndPassword(userName = "Илья", password = "Муромец") AuthHelper authHelper) throws IOException {
        System.out.println("Авторизация гостя");
        assertEquals(401, authHelper.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("Проверка авторизации клиента возвращает код 401")
    @Description("Проверяем авторизацию гостя без userName и password")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    void authorizationEmpty(@AuthUserNameAndPassword(userName = "", password = "") AuthHelper authHelper) throws IOException {
        System.out.println("Авторизация гостя");
        assertEquals(401, authHelper.getResponse().getStatusCode());
    }
}
