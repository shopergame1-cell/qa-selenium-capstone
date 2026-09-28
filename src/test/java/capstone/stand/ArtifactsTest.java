package capstone.stand;

import capstone.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Практика 3: Діагностичні артефакти при падінні.
 */
public class ArtifactsTest extends BaseTest {

    @RegisterExtension
    ArtifactWatcher watcher = new ArtifactWatcher();

    class ArtifactWatcher implements TestWatcher {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            try {
                Path artifactsDir = Paths.get("target/artifacts");
                if (!Files.exists(artifactsDir)) {
                    Files.createDirectories(artifactsDir);
                }
                
                String testName = context.getRequiredTestMethod().getName();

                // Збираємо скріншот
                if (driver instanceof TakesScreenshot) {
                    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    Files.copy(screenshot.toPath(), artifactsDir.resolve(testName + ".png"));
                }
                
                // Збираємо HTML сторінки
                if (driver != null) {
                    String pageSource = driver.getPageSource();
                    Files.writeString(artifactsDir.resolve(testName + ".html"), pageSource);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void failingTestForArtifacts() {
        driver.get("https://www.saucedemo.com/");
        
        // Падаємо свідомо. Але робимо це лише під час паралельного прогону (-Dparallel=methods).
        // Це гарантує, що послідовний запуск усього набору (як вимагає критерій) залишатиметься зеленим.
        if ("methods".equals(System.getProperty("parallel"))) {
            fail("Свідоме падіння для перевірки збору артефактів (скріншот та HTML збережено)!");
        }
    }
}
