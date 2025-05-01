package swag_labs.page_object;

import base.BaseSelenide;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.*;

public class ProductsPage extends BaseSelenide {

    private final ElementsCollection productElements = getCollectionByClassName("inventory_item");
    private final String urlProductPage = getUrlPage("urlProductPage");
    private final List<String> productNames;

    public ProductsPage(List<String> productNames) {
        this.productNames = productNames;
    }

    @Step("Получить URL текущей страницы {urlProductPage}")
    public String getUrlProductPage() {
        return urlProductPage;
    }

    @Step("Проверить переход на страницу Продукты")
    public void checkProductsPage() {
        isCurrentUrlPage("urlProductPage");
    }

    @Step("Добавить список выбранных товаров в корзину")
    public int addToCard() {
        int count = 0;
        for (String productName : productNames) {

            for (SelenideElement element : productElements) {
                String productTitle = findElementByClassNameText(element, "inventory_item_name") ;

                if(productTitle.equals(productName)){
                    attachData(element.getText());
                    clickButtonByTagName(element, "button");
                    attachData(element.getText());
                    count++;
                }
            }
        }
        return count;
    }

    @Step("Сравнить количество добавленных продуктов и количество продуктов в корзине")
    public void addToCardAndCheck() {
        String shoppingCount = String.valueOf(addToCard());
        $(byClassName("shopping_cart_badge")).shouldHave(text(shoppingCount));
    }

    @Step("Открыть страницу Продукты")
    public void open() {
        Selenide.open(urlProductPage);
    }
}
