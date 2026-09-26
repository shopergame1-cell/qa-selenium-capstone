package capstone;

import capstone.pages.InventoryPage;
import capstone.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** 5 тестів, які мусять стати зеленими. Кожен — окремий сценарій. */
class LoginTest extends BaseTest {

    private LoginPage login() {
        return new LoginPage(driver).open();
    }

    @Test
    @DisplayName("1. Успішний логін відкриває список товарів (>= 6 позицій)")
    void successfulLoginShowsInventory() {
        LoginPage page = login().typeUsername("standard_user").typePassword("secret_sauce");
        page.submit();
        InventoryPage inventory = new InventoryPage(driver).waitUntilOpened();
        assertTrue(inventory.itemCount() >= 6, "у списку мусить бути щонайменше 6 товарів");
    }

    @Test
    @DisplayName("2. Невірний пароль показує повідомлення про помилку")
    void wrongPasswordShowsError() {
        LoginPage page = login().typeUsername("standard_user").typePassword("wrong_password");
        page.submit();
        assertTrue(page.errorMessage().toLowerCase().contains("username and password do not match"),
                "мусить бути повідомлення про невірний логін/пароль");
    }

    @Test
    @DisplayName("3. Порожній логін показує помилку про обовʼязкове поле")
    void emptyUsernameShowsError() {
        LoginPage page = login().typePassword("secret_sauce");
        page.submit();
        assertTrue(page.errorMessage().toLowerCase().contains("username is required"),
                "мусить бути повідомлення про обовʼязковий логін");
    }

    @Test
    @DisplayName("4. Заблокований користувач не може увійти")
    void lockedOutUserCannotLogin() {
        LoginPage page = login().typeUsername("locked_out_user").typePassword("secret_sauce");
        page.submit();
        assertTrue(page.errorMessage().toLowerCase().contains("locked out"),
                "мусить бути повідомлення про заблокованого користувача");
    }

    @Test
    @DisplayName("5. Додавання товару оновлює бейдж кошика")
    void addingItemUpdatesCartBadge() {
        LoginPage page = login().typeUsername("standard_user").typePassword("secret_sauce");
        page.submit();
        InventoryPage inventory = new InventoryPage(driver).waitUntilOpened().addBackpackToCart();
        assertEquals(1, inventory.cartBadgeCount(), "на бейджі кошика мусить бути 1");
    }
}
