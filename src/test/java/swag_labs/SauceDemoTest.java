package swag_labs;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import swag_labs.page_object.CardPage;
import swag_labs.page_object.CheckOutCompletePage;
import swag_labs.page_object.CheckOutPage;
import swag_labs.page_object.CheckOutStepTwoPage;
import swag_labs.page_object.MainPage;
import swag_labs.page_object.ProductsPage;

import java.util.List;

@Owner("Валентин Врублевский")
@Epic("Онлайн-магазин товаров")
@DisplayName("Тест для добавления товаров в корзину и оформления покупки")
public class SauceDemoTest {

    private MainPage mainPage;
    private ProductsPage productsPage;
    private CardPage cardPage;
    private CheckOutPage checkOutPage;
    private CheckOutStepTwoPage checkOutStepTwoPage;
    private CheckOutCompletePage checkOutCompletePage;

    private final String totalSum = "$58.29";
    private final List<String> productName = List.of("Sauce Labs Backpack", "Sauce Labs Bolt T-Shirt", "Sauce Labs Onesie");

    @BeforeAll
    public static void beforeAll() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        Configuration.pageLoadStrategy = "eager";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1024x768";
        Configuration.headless = true;
    }

    @BeforeEach
    public void setUp() {
        mainPage = new MainPage();
        productsPage = new ProductsPage(productName);
        cardPage = new CardPage();
        checkOutPage = new CheckOutPage();
        checkOutStepTwoPage = new CheckOutStepTwoPage();
        checkOutCompletePage = new CheckOutCompletePage();
        mainPage.open();
    }

    @Test
    @DisplayName("Проверка URL")
    @Description("Проверяем, что мы находимся на нужной странице")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    public void testRedirect() {
        mainPage.isCurrentUrlPage("urlMainPage");
    }

    @Test
    @DisplayName("Проверка названия страницы")
    @Description("Проверяем, что мы находимся на странице Swag Labs")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    public void testTitle() {
        mainPage.isByClassNameExistsText("login_logo", "Swag Labs");
    }

    @Test
    @DisplayName("Успешная авторизация")
    @Description("Авторизация для пользователя standard_user")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    public void testSuccessfulAuthorization() {
        mainPage.login("standard_user", "secret_sauce");
        mainPage.screenShot();
        mainPage.isNotExists("h3");
        mainPage.isCurrentUrlPage("urlProductPage");
    }

    @Test
    @DisplayName("Авторизация заблокированного пользователя")
    @Description("Авторизация для пользователя locked_out_user")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Негативный")
    public void testLockedOutUserAuthorization() {
        mainPage.login("locked_out_user", "secret_sauce");
        mainPage.screenShot();
        mainPage.isSelectorConsistText("h3", "Epic sadface: Sorry, this user has been locked out.");
    }

    @ParameterizedTest
    @CsvSource({"standard_user, secret_sauce", "performance_glitch_user, secret_sauce"})
    @DisplayName("Сквозное тестирование")
    @Description("Тестирование всех этапов")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("Позитивный")
    public void testEndToEnd(String user, String password) {
        mainPage.login(user, password);

        productsPage.checkProductsPage();
        productsPage.addToCardAndCheck();

        cardPage.open();
        cardPage.goToCheckOutPage();

        checkOutPage.checkOutForm();
        checkOutPage.checkOutFormContinue();

        checkOutStepTwoPage.checkTotalSum(totalSum);
        checkOutStepTwoPage.finish();

        checkOutCompletePage.isComplete();
    }
}
