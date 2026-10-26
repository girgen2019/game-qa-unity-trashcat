package io.github.girgen2019.trashcat.performance;

import io.github.girgen2019.trashcat.config.TestConfig;
import io.github.girgen2019.trashcat.device.Adb;
import io.github.girgen2019.trashcat.device.AdbOutput;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Basic performance checks on a real Android device over ADB.
 * Thresholds live in config.properties; the measured values are attached to the Allure report
 * so they can be compared between builds even when the test passes.
 */
@Tag("android")
@Feature("Android performance")
@DisplayName("Android: performance")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AndroidPerformanceTest {

    private static final String PACKAGE = TestConfig.androidPackage();

    private static Adb adb;
    private static boolean deviceConnected;

    @BeforeAll
    static void connectToDevice() {
        adb = new Adb();
        deviceConnected = !adb.connectedDevices().isEmpty();
    }

    @BeforeEach
    void requireDevice() {
        assumeTrue(deviceConnected, "No authorized Android device connected - skipping performance tests");
    }

    @Test
    @Order(1)
    @DisplayName("PERF-01 Cold start is faster than the threshold")
    @Description("Force-stop, then launch with 'am start -W' and read TotalTime: the time until the game "
            + "activity is drawn. Must be below android.maxColdStartMs.")
    void coldStartIsFast() {
        Allure.step("Force-stop " + PACKAGE + " for a cold start", () -> adb.forceStop(PACKAGE));
        int ms = adb.launchAndMeasure(PACKAGE);
        Allure.addAttachment("Cold start time", "text/plain", ms + " ms", ".txt");

        assertThat(ms)
                .as("Cold start time, ms (threshold %d)", TestConfig.maxColdStartMs())
                .isLessThanOrEqualTo(TestConfig.maxColdStartMs());
    }

    @Test
    @Order(2)
    @DisplayName("PERF-02 Memory use after 30 s is below the threshold")
    @Description("Let the game run for android.stabilitySec seconds, then read TOTAL PSS from "
            + "'dumpsys meminfo'. Must be below android.maxMemoryMb.")
    void memoryUseIsBelowThreshold() throws InterruptedException {
        if (adb.pidOf(PACKAGE).isEmpty()) {
            Allure.step("Game is not running - launch it", () -> adb.launch(PACKAGE));
        }
        int seconds = TestConfig.stabilitySec();
        Allure.step("Let the game run for " + seconds + " s");
        Thread.sleep(seconds * 1000L);

        String meminfo = adb.memoryInfo(PACKAGE);
        Allure.addAttachment("dumpsys meminfo", "text/plain", meminfo, ".txt");
        int mb = AdbOutput.totalPssKb(meminfo) / 1024;
        Allure.addAttachment("Memory (TOTAL PSS)", "text/plain", mb + " MB", ".txt");

        assertThat(mb)
                .as("Memory use (TOTAL PSS), MB (threshold %d)", TestConfig.maxMemoryMb())
                .isLessThanOrEqualTo(TestConfig.maxMemoryMb());
    }
}
