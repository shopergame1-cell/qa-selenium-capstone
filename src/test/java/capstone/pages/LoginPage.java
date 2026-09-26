package capstone.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Page Object сторінки логіну Swag Labs (еталонна реалізація). */
public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public LoginPage open() {
        driver.get("https://www.saucedemo.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME));
        return this;
    }

    public LoginPage typeUsername(String username) {
        wait.until(ExpectedConditions.elementToBeClickable(USERNAME)).sendKeys(username);
        return this;
    }

    public LoginPage typePassword(String password) {
        wait.until(ExpectedConditions.elementToBeClickable(PASSWORD)).sendKeys(password);
        return this;
    }

    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON)).click();
    }

    public String errorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR)).getText();
    }
}
