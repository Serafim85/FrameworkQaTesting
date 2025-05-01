package swag_labs.page_object;

import base.BaseSelenide;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardPage extends BaseSelenide {

    private final String urlCardPage = getUrlPage("urlCardPage");
    private final ElementsCollection cardElements = getCollectionByClassName("cart_item");

    @Step("Получить URL текущей страницы")
    public String getUrlCardPage() {
        return urlCardPage;
    }

    @Step("Получить коллекцию выбранных продуктов")
    public ElementsCollection getCardElements() {
        return cardElements;
    }

    @Step("Количество выбранных продуктов в корзине")
    public String getCardElementsSize() {
        Configuration.timeout = 2000;
        return String.valueOf(cardElements.size());
    }

    @Step("Открыть страницу Корзина")
    public void open() {
        Selenide.open(urlCardPage);
        screenShot();
        String shoppingCardCount = getTextByClassName("shopping_cart_badge");
        assertEquals(shoppingCardCount, getCardElementsSize());
    }

    @Step("Перейти на страницу Дополнительной информации")
    public void goToCheckOutPage() {
        clickByIdName("checkout");
        screenShot();
        isCurrentUrlPage("urlCheckOutPage");
    }
}
