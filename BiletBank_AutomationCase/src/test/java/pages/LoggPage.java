package pages;

import config.TestConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoggPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By acceptContractInput = By.id("acceptContract");
    private final By acceptContractLabel = By.cssSelector("label[for='acceptContract']");
    private final By devamEtBtn = By.xpath("//a[contains(@class,'bb-btn') and contains(@class,'bb-btn--blue') and normalize-space()='Devam Et']");

    public LoggPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.WAIT_SEC));
    }

    public void acceptContractAndContinue() {
        WebElement labelContrat = wait.until(ExpectedConditions.elementToBeClickable(acceptContractLabel));
        clickRobuste(labelContrat);

        WebElement cbs = wait.until(ExpectedConditions.presenceOfElementLocated(acceptContractInput));
        if (!cbs.isSelected()) {
            jsClick(cbs);
        }

        WebElement devamEt = wait.until(ExpectedConditions.elementToBeClickable(devamEtBtn));
        clickRobuste(devamEt);
    }

    private void clickRobuste(WebElement el) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (ElementClickInterceptedException | TimeoutException e) {
            scrollIntoView(el);
            try {
                el.click();
            } catch (Exception ex) {
                jsClick(el);
            }
        }
    }

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }
}