package capstone.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** Page Object списку товарів (після успішного логіну). */
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
        throw new UnsupportedOperationException("TODO: дочекайся, що список товарів зʼявився (ITEMS)");
    }

    public int itemCount() {
        throw new UnsupportedOperationException("TODO: поверни кількість товарів на сторінці");
    }

    public InventoryPage addBackpackToCart() {
        throw new UnsupportedOperationException("TODO: додай рюкзак у кошик");
    }

    public int cartBadgeCount() {
        throw new UnsupportedOperationException("TODO: поверни число на бейджі кошика (0, якщо бейджа немає)");
    }

    public void openCart() {
        throw new UnsupportedOperationException("TODO: відкрий кошик");
    }
}
