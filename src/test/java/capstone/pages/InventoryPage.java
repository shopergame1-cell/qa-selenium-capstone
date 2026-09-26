package capstone.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** Page Object списку товарів (еталонна реалізація). */
public class InventoryPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By ITEMS = By.cssSelector("[data-test='inventory-item']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private static final By ADD_BACKPACK = By.id("add-to-cart-sauce-labs-backpack");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public InventoryPage waitUntilOpened() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(ITEMS));
        return this;
    }

    public int itemCount() {
        return driver.findElements(ITEMS).size();
    }

    public InventoryPage addBackpackToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(ADD_BACKPACK)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_BADGE));
        return this;
    }

    public int cartBadgeCount() {
        List<WebElement> badges = driver.findElements(CART_BADGE);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText().trim());
    }

    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(CART_LINK)).click();
    }
}
