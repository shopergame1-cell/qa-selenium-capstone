package capstone.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object сторінки логіну Swag Labs (https://www.saucedemo.com/).
 *
 * TODO: реалізуй методи цього Page Object. Правила:
 *  - ніяких Thread.sleep: користуйся WebDriverWait/ExpectedConditions;
 *  - локатори — у константах, не розсипані по тестах;
 *  - методи повертають цей самий обʼєкт або наступну сторінку (fluent-стиль).
 */
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
        throw new UnsupportedOperationException("TODO: відкрий https://www.saucedemo.com/ і дочекайся появи поля логіну");
    }

    public LoginPage typeUsername(String username) {
        throw new UnsupportedOperationException("TODO: введи логін у поле user-name");
    }

    public LoginPage typePassword(String password) {
        throw new UnsupportedOperationException("TODO: введи пароль у поле password");
    }

    public void submit() {
        throw new UnsupportedOperationException("TODO: натисни login-button");
    }

    public String errorMessage() {
        throw new UnsupportedOperationException("TODO: поверни текст помилки з [data-test='error']");
    }
}
