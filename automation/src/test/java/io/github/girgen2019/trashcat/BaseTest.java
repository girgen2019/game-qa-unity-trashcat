package io.github.girgen2019.trashcat;

import com.alttester.AltDriver;
import io.github.girgen2019.trashcat.driver.DriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Base for in-game tests through AltTester®: connects to the running game once per test class.
 * Needs the game build with the AltTester SDK running and AltTester Desktop on the PC
 * (for Android over USB: adb reverse tcp:13000 tcp:13000).
 * Without a connection the tests are reported as skipped, not failed.
 */
@Tag("alttester")
public abstract class BaseTest {

    protected static AltDriver driver;
    private static String connectionError;

    @BeforeAll
    static void connect() {
        try {
            driver = DriverManager.get();
            connectionError = null;
        } catch (RuntimeException e) {
            // Do not fail here: an exception in @BeforeAll hides the tests from the report.
            driver = null;
            connectionError = e.getClass().getSimpleName() + ": " + e.getMessage();
        }
    }

    @BeforeEach
    void requireConnection() {
        assumeTrue(driver != null, () -> "No AltTester connection - skipping in-game tests (" + connectionError + ")");
    }

    @AfterAll
    static void disconnect() {
        DriverManager.quit();
    }
}
