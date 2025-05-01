package swag_labs.page_object;

import base.BaseSelenide;
import io.qameta.allure.Step;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckOutCompletePage extends BaseSelenide {

    @Step("Получить URL текущей страницы {urlCheckOutCompletePage}")
    public String getUrlCheckOutCompletePage() {
        return getUrlPage("urlCheckOutCompletePage");
    }

    @Step("Проверить переход на страницу Завершение(CheckOutComplete)")
    public void isComplete(){
        screenShot();
        isCurrentUrlPage("urlCheckOutCompletePage");
        String messageHeader = getTextByClassName("complete-header");

        assertEquals("Thank you for your order!", messageHeader);
        System.out.println(messageHeader);
    }
}
