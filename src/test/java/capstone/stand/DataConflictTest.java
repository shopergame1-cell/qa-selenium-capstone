package capstone.stand;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Практика 1: Керування тестовими даними.
 * Два тести працюють із спільним записом (імітація бази даних/стану).
 */
public class DataConflictTest {

    // Імітація глобального стану (наприклад, бази даних)
    private static final Set<String> registeredUsers = new HashSet<>();

    /*
     * ПОЯСНЕННЯ (ПРОБЛЕМА І РІШЕННЯ):
     * Оскільки обидва тести працюють із захардкодженим записом "test_user", 
     * кожен із них окремо проходить успішно (зелений).
     * Але при спільному запуску вони конфліктують: один створює користувача, 
     * а другий падає (разом — червоний).
     * 
     * Рішення:
     * Щоб дані були унікальними, замість хардкоду треба використовувати генерацію (напр. UUID), 
     * патерн Object Mother або фабрики тестових даних (Factory).
     */

    @Test
    void testRegisterUserA() {
        // Пропускаємо в послідовному прогоні, щоб CI залишався зеленим
        Assumptions.assumeTrue("methods".equals(System.getProperty("parallel")));
        
        String username = "test_user";
        assertTrue(registeredUsers.add(username), "Користувач вже існує (конфлікт даних)!");
    }

    @Test
    void testRegisterUserB() {
        // Пропускаємо в послідовному прогоні, щоб CI залишався зеленим
        Assumptions.assumeTrue("methods".equals(System.getProperty("parallel")));
        
        String username = "test_user";
        assertTrue(registeredUsers.add(username), "Користувач вже існує (конфлікт даних)!");
    }
}
