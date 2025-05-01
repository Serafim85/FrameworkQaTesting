package swag_labs.page_object;

import base.BaseSelenide;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

public class MainPage extends BaseSelenide {

    private final String urlMainPage = getUrlPage("urlMainPage");

    @Step("Открыть страницу регистрации")
    public void open() {
        Selenide.open(urlMainPage);
    }

    @Step("Регистрация на странице пользователя {user}")
    public void login(String user, String password) {
        setValueByName("user-name", user);
        setValueByName("password", password);
        attachImage(getElementByValueName("Login"));
        clickByValueName("Login");
        isNotExists("h3");
    }
}
