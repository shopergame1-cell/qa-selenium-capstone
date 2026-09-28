package capstone.stand;

import capstone.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Практика 2: Паралельний запуск і нестабільні тести.
 */
public class ParallelismTest extends BaseTest {
    
    // Спільний стан, через який тести ламаються при паралельному запуску.
    private static String sharedUserSession;

    // Синхронізатор для гарантованого відтворення гонки даних.
    private static final CountDownLatch latch = new CountDownLatch(2);

    /*
     * ПОЯСНЕННЯ (Як цю проблему лікують правильно):
     * Використання статичних полів як спільного стану між тестами призводить до гонки даних 
     * при паралельному виконанні.
     * 
     * Правильні підходи:
     * 1. Ізоляція стану екземпляром: використовувати поля екземпляра класу (не статичні). 
     *    JUnit 5 за замовчуванням створює новий екземпляр класу для кожного тестового методу.
     * 2. Локальний стан: тримати стан повністю у локальних змінних методу.
     * 3. ThreadLocal<String>: якщо стан дійсно має бути статичним або глобальним, 
     *    використовувати ThreadLocal, щоб кожен потік мав власну незалежну копію змінної.
     */

    @Test
    void testSessionOne() throws InterruptedException {
        sharedUserSession = "session_1";
        
        // Гарантуємо, що обидва потоки опиняться в критичній секції одночасно,
        // усуваючи залежність від швидкості машини (детерміноване падіння).
        if ("methods".equals(System.getProperty("parallel"))) {
            latch.countDown();
            latch.await(5, TimeUnit.SECONDS);
        }
        
        assertEquals("session_1", sharedUserSession, 
                "Гонка даних! Спільний стан (статичне поле sharedUserSession) " +
                "був перезаписаний іншим потоком. Очікували 'session_1', " +
                "але отримали: '" + sharedUserSession + "'");
    }

    @Test
    void testSessionTwo() throws InterruptedException {
        sharedUserSession = "session_2";
        
        if ("methods".equals(System.getProperty("parallel"))) {
            latch.countDown();
            latch.await(5, TimeUnit.SECONDS);
        }
        
        assertEquals("session_2", sharedUserSession, 
                "Гонка даних! Спільний стан (статичне поле sharedUserSession) " +
                "був перезаписаний іншим потоком. Очікували 'session_2', " +
                "але отримали: '" + sharedUserSession + "'");
    }
}
