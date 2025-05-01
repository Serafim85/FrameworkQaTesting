package base;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Attachment;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverConditions.url;

public abstract class BaseSelenide {
    private final BaseProperties properties;
    {
        try {
            properties = new BaseProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getUrlPage(String urlPage) {
        return properties.getProperties().getProperty(urlPage);
    }

    protected ElementsCollection getCollectionByClassName(String className) {
        return $$(byClassName(className));
    }

    protected SelenideElement getElementByIdName(String idName) {
        return $(byId(idName));
    }

    protected SelenideElement getElementByClassName(String byClassName) {
        return $(byClassName(byClassName));
    }

    protected SelenideElement getElementByValueName(String byValueName) {
        return $(byValue(byValueName));
    }

    protected String findElementByClassNameText(SelenideElement element, String byClassName) {
        return element.find(By.className(byClassName)).getText();
    }

    public String getTextByClassName(String className) {
        return $(byClassName(className)).getText();
    }

    protected void setValueByName(String name, String value) {
        $(byName(name)).setValue(value);
    }

    protected void clickByIdName(String idName) {
        $(byId(idName)).click();
    }

    protected void clickByValueName(String byValueName) {
        $(byValue(byValueName)).click();
    }

    protected void clickButtonByTagName(SelenideElement element, String tagNameButton) {
        element.find(By.tagName("button")).click();
    }

    public void isCurrentUrlPage(String currentUrlPage) {
        webdriver().shouldHave(url(getUrlPage(currentUrlPage)));
    }

    public void isSelectorConsistText(String selector, String text) {
        $(selector).shouldHave(text(text));
    }

    public void isByClassNameExistsText(String byClassName, String text) {
        $(byClassName(byClassName)).shouldHave(text(text));
    }

    public void isNotExists(String selector) {
        $(selector).shouldNot();
    }

    protected void isExists(String selector) {
        $(selector).should();
    }

    @Attachment(value = "data", type = "text/plain", fileExtension = ".txt")
    public String attachData(String text) {
        return text;
    }

    @Attachment(value = "data", type = "image/png", fileExtension = ".png")
    public byte[] attachImage(SelenideElement element) {
        return element.getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page Screenshot", type = "image/png")
    public byte[] screenShot() {
        WebDriver driver = webdriver().driver().getWebDriver();
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
