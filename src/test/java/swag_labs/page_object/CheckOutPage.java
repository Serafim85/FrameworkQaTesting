package swag_labs.page_object;

import base.BaseSelenide;
import io.qameta.allure.Step;

public class CheckOutPage extends BaseSelenide {

    @Step("Заполнить личную информацию")
    public void checkOutForm(){
        setValueByName("firstName","Валентин");
        setValueByName("lastName","Врублевский");
        setValueByName("postalCode","123456");
        screenShot();
    }

    @Step("Нажать кнопку Continue и перейти на страницу Продолжение(CheckOutStepTwo)")
    public void checkOutFormContinue(){
        String idName = "continue";
        attachImage(getElementByIdName(idName));
        clickByIdName(idName);
        screenShot();
    }

    @Step("Получить URL текущей страницы")
    public String getUrlCheckOutPage() {
        return getUrlPage("urlCheckOutPage");
    }
}
