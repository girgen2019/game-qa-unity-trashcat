package io.github.girgen2019.trashcat.smoke;

import io.github.girgen2019.trashcat.config.TestConfig;
import io.github.girgen2019.trashcat.device.Adb;
import io.github.girgen2019.trashcat.device.CrashDetector;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Smoke checks on a real Android device over ADB.
 * They do not need AltTester Desktop: they verify the build from the OS point of view
 * (installed, starts, comes to the foreground, does not crash).
 * Tests run in order and share one launch of the game, like a manual smoke pass.
 */
@Tag("android")
@Feature("Android smoke")
@DisplayName("Android: game launch smoke")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AndroidLaunchSmokeTest {

    private static final String PACKAGE = TestConfig.androidPackage();

    private static Adb adb;
    private static Integer launchedPid;

    @BeforeAll
    static void connectToDevice() {
        adb = new Adb();
        List<String> devices = adb.connectedDevices();
        assumeTrue(!devices.isEmpty(), "No authorized Android device connected - skipping device smoke tests");
        System.out.println("Running device smoke on: " + devices.get(0));
    }

    @Test
    @Order(1)
    @DisplayName("SMK-01 Game is installed on the device")
    void gameIsInstalled() {
        assertThat(adb.isInstalled(PACKAGE))
                .as("Package %s must be installed", PACKAGE)
                .isTrue();
    }

    @Test
    @Order(2)
    @DisplayName("SMK-02 Game starts and comes to the foreground")
    @Description("Cold start: force-stop, clear logcat, launch from the launcher, wait for the game activity on top")
    void gameStartsAndComesToForeground() throws InterruptedException {
        Allure.step("Cold start: force-stop and clear logcat", () -> {
            adb.forceStop(PACKAGE);
            adb.clearLogcat();
        });
        Allure.step("Launch " + PACKAGE, () -> adb.launch(PACKAGE));

        String topActivity = waitForForeground(TestConfig.launchTimeoutSec());
        attachScreenshot("After launch");

        assertThat(topActivity)
                .as("Game activity must be on top within %d s", TestConfig.launchTimeoutSec())
                .contains(PACKAGE);

        Optional<Integer> pid = adb.pidOf(PACKAGE);
        assertThat(pid).as("Game process must be running").isPresent();
        launchedPid = pid.get();
    }

    @Test
    @Order(3)
    @DisplayName("SMK-03 Game keeps running without crashes")
    @Description("The same process stays alive and logcat has no crash/ANR/unhandled exception markers")
    void gameKeepsRunningWithoutCrashes() throws InterruptedException {
        assumeTrue(launchedPid != null, "Game was not launched by SMK-02");

        int seconds = TestConfig.stabilitySec();
        Allure.step("Let the game run for " + seconds + " s");
        Thread.sleep(seconds * 1000L);

        String logcat = adb.logcat();
        Allure.addAttachment("logcat", "text/plain", logcat, ".txt");
        attachScreenshot("After " + seconds + " s");

        assertThat(adb.pidOf(PACKAGE))
                .as("Game process must not restart or die (pid %d at launch)", launchedPid)
                .contains(launchedPid);
        assertThat(CrashDetector.findCrashLines(logcat, PACKAGE))
                .as("Crash markers in logcat")
                .isEmpty();
    }

    private static String waitForForeground(int timeoutSec) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutSec * 1000L;
        String top = "";
        while (System.currentTimeMillis() < deadline) {
            top = adb.topResumedActivity();
            if (top.contains(PACKAGE)) {
                return top;
            }
            Thread.sleep(1000);
        }
        return top;
    }

    private static void attachScreenshot(String name) {
        byte[] png = adb.screenshot();
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(png), ".png");
    }
}
