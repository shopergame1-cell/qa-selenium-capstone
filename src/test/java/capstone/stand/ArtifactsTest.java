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
            String testName = context.getRequiredTestMethod().getName();
            Path artifactsDir = Paths.get("target", "artifacts");

            try {
                Files.createDirectories(artifactsDir);
            } catch (Exception e) {
                System.err.println("Не вдалося створити директорію для артефактів: " + e.getMessage());
                return;
            }

            // Збираємо причину падіння (лог)
            try {
                java.io.StringWriter sw = new java.io.StringWriter();
                if (cause != null) {
                    cause.printStackTrace(new java.io.PrintWriter(sw));
                }
                Files.writeString(artifactsDir.resolve(testName + ".log"), "Причина падіння:\n" + sw.toString());
            } catch (Exception e) {
                System.err.println("Не вдалося зберегти лог падіння: " + e.getMessage());
            }

            // Збираємо скріншот
            try {
                if (driver instanceof TakesScreenshot) {
                    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    Files.copy(screenshot.toPath(), artifactsDir.resolve(testName + ".png"), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception e) {
                System.err.println("Не вдалося зберегти скріншот: " + e.getMessage());
            }
            
            // Збираємо HTML сторінки
            try {
                if (driver != null) {
                    String pageSource = driver.getPageSource();
                    Files.writeString(artifactsDir.resolve(testName + ".html"), pageSource);
                }
            } catch (Exception e) {
                System.err.println("Не вдалося зберегти HTML: " + e.getMessage());
            }
        }
    }

    @Test
    void failingTestForArtifacts() {
        driver.get("https://www.saucedemo.com/");
        
        // Падаємо свідомо. Але робимо це лише під час паралельного прогону (-Dparallel=methods).
        // Це гарантує, що послідовний запуск усього набору (як вимагає критерій) залишатиметься зеленим.
        if ("methods".equals(System.getProperty("parallel"))) {
            fail("Свідоме падіння для перевірки збору артефактів!");
        }
    }
}
