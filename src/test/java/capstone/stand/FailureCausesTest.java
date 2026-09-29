package capstone.stand;

import capstone.BaseTest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Практика 4: падіння з різних причин — матеріал для діагностики.
 *
 * Сенс класу: навчитися читати падіння, а не перезапускати тест. Три тести падають ЗАВЖДИ, і кожен
 * зі своєї причини — на екрані, в HTML сторінки, у логах і в мережевому лозі ці причини виглядають
 * по-різному. Щоб студент бачив різницю, стенд піднімається локально в самому тесті (звичайний
 * HttpServer з JDK на випадковому порту): жодної залежності від зовнішніх сайтів і мережі, тому
 * падіння відтворюються однаково і локально, і в CI.
 *
 *  1. elementAppearedTooLate — елемент зʼявляється пізніше, ніж тест його чекає (TimeoutException).
 *     Дивитись: скріншот (порожнє місце, де мала бути кнопка), HTML (елемента ще немає), консоль
 *     (код, що вставляє кнопку через setTimeout), мережевий лог (усі запити завершились успішно —
 *     тобто справа не в мережі, а в часі).
 *
 *  2. pageTextChanged — текст на сторінці змінився (AssertionError з різницею).
 *     Дивитись: скріншот (елемент на місці, текст інший), HTML (справжній текст), історію макета.
 *
 *  3. backendReturnedError — бекенд віддав помилку (AssertionError із текстом помилки від API).
 *     Дивитись: мережевий лог (500 від /api/orders) і текст у #status — сторінка чесно показала, що
 *     дані не прийшли.
 */
public class FailureCausesTest extends BaseTest {

    private static HttpServer stand;
    private static String base;

    @BeforeAll
    static void startStand() throws IOException {
        stand = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);

        // Причина 1: кнопка підтвердження зʼявляється через 3 секунди після завантаження сторінки.
        stand.createContext("/late", exchange -> html(exchange, """
                <!doctype html>
                <html lang="uk"><head><meta charset="utf-8"><title>Підтвердження замовлення</title></head>
                <body>
                  <h1>Підтвердження замовлення</h1>
                  <p>Перевірте дані й підтвердьте.</p>
                  <div id="confirm"></div>
                  <script>
                    setTimeout(function () {
                      document.getElementById('confirm').innerHTML = '<button id="confirm-button">Підтвердити</button>';
                    }, 3000);
                  </script>
                </body></html>"""));

        // Причина 2: текст суми вже інший, ніж той, на який розраховує тест.
        stand.createContext("/changed", exchange -> html(exchange, """
                <!doctype html>
                <html lang="uk"><head><meta charset="utf-8"><title>Кошик</title></head>
                <body>
                  <h1>Кошик</h1>
                  <p id="total">Разом: 1250 ₴ (з доставкою)</p>
                </body></html>"""));

        // Причина 3: сторінка чекає список замовлень з API, а API відповідає 500.
        stand.createContext("/orders", exchange -> html(exchange, """
                <!doctype html>
                <html lang="uk"><head><meta charset="utf-8"><title>Мої замовлення</title></head>
                <body>
                  <h1>Мої замовлення</h1>
                  <table><tbody id="orders"></tbody></table>
                  <div id="status"></div>
                  <script>
                    fetch('/api/orders').then(function (response) {
                      return response.text().then(function (body) {
                        if (response.ok) {
                          document.getElementById('orders').innerHTML = body;
                        } else {
                          document.getElementById('status').textContent =
                            'Помилка: ' + response.status + ' ' + body;
                        }
                      });
                    });
                  </script>
                </body></html>"""));

        stand.createContext("/api/orders", exchange -> {
            byte[] body = "Внутрішня помилка сервісу: база даних недоступна".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(500, body.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(body);
            }
        });

        stand.start();
        base = "http://127.0.0.1:" + stand.getAddress().getPort();
    }

    @AfterAll
    static void stopStand() {
        if (stand != null) {
            stand.stop(0);
        }
    }

    private static void html(HttpExchange exchange, String page) throws IOException {
        byte[] body = page.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }

    /** Причина 1: елемент зʼявився пізно — тест чекає 1 секунду, кнопка приходить через 3. */
    @Test
    void elementAppearedTooLate() {
        driver.get(base + "/late");

        new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("confirm-button")));
    }

    /** Причина 2: текст змінився — тест звіряє зі старим формулюванням. */
    @Test
    void pageTextChanged() {
        driver.get(base + "/changed");

        String total = driver.findElement(By.id("total")).getText();

        assertEquals("Разом: 1200 ₴", total, "Сума в кошику змінилась — уточни очікування або зʼясуй, чи це не регрес");
    }

    /** Причина 3: помилка з API — сторінка показала помилку замість даних. */
    @Test
    void backendReturnedError() {
        driver.get(base + "/orders");

        new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> {
            String status = d.findElement(By.id("status")).getText();
            boolean rowsLoaded = !d.findElements(By.cssSelector("#orders tr")).isEmpty();
            return !status.isEmpty() || rowsLoaded;
        });

        String status = driver.findElement(By.id("status")).getText();

        assertTrue(status.isEmpty(), "Список замовлень не завантажився, сторінка показала: " + status);
    }
}
