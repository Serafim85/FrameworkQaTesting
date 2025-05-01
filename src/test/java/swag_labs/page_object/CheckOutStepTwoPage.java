package swag_labs.page_object;

import base.BaseSelenide;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckOutStepTwoPage extends BaseSelenide {

    private final String urlCheckOutStepTwoPage = getUrlPage("urlCheckOutStepTwoPage");

    @Step("Получить URL текущей страницы")
    public String getUrlCheckOutStepTwoPage() {
        return urlCheckOutStepTwoPage;
    }

    @Step("Открыть страницу Проверка данных(продолжение)")
    public void open() {
        Selenide.open(urlCheckOutStepTwoPage);
    }

    @Step("Получить общую сумму покупки {totalSumCard}")
    public String getTotalSum() {
        attachImage(getElementByClassName("summary_total_label"));
        String totalSum = getTextByClassName("summary_total_label");
        String[] total = totalSum.split(" ");
        return total[1];
    }

    @Step("Сравнить общую сумму покупки {totalSumCard} с контрольной суммой {totalSum}")
    public void checkTotalSum(String totalSum) {
        isCurrentUrlPage("urlCheckOutStepTwoPage");
        assertEquals(totalSum, getTotalSum());
    }

    @Step("Закончить работу. Нажать кнопку Finish")
    public void finish() {
        clickByIdName("finish");
    }
}
