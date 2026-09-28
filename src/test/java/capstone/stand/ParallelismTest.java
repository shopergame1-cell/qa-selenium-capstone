package capstone.stand;

import capstone.BaseTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Практика 2: Паралельний запуск і нестабільні тести.
 */
public class ParallelismTest extends BaseTest {
    
    // Спільний стан, через який тести ламаються при паралельному запуску.
    private static String sharedUserSession;

    @Test
    void testSessionOne() throws InterruptedException {
        sharedUserSession = "session_1";
        
        // Імітація довгої роботи (наприклад, відкриття сторінки), 
        // що дає час іншому потоку перезаписати спільний стан
        Thread.sleep(2000); 
        
        // При послідовному запуску значення залишається "session_1" (зелено).
        // При паралельному інший потік перезапише його на "session_2" (червоно).
        assertEquals("session_1", sharedUserSession, 
                "Спільний стан (статичне поле) був змінений іншим потоком! Це показує проблему паралелізму.");
    }

    @Test
    void testSessionTwo() throws InterruptedException {
        sharedUserSession = "session_2";
        
        Thread.sleep(2000); 
        
        assertEquals("session_2", sharedUserSession, 
                "Спільний стан (статичне поле) був змінений іншим потоком! Це показує проблему паралелізму.");
    }
}
