package io.github.girgen2019.trashcat.driver;

import com.alttester.AltDriver;
import io.github.girgen2019.trashcat.config.TestConfig;

import java.io.IOException;

/**
 * Owns the single AltDriver connection to the game.
 * The game build must contain the AltTester Unity SDK and AltTester Desktop must be running.
 */
public final class DriverManager {

    private static AltDriver driver;

    private DriverManager() {
    }

    public static synchronized AltDriver get() {
        if (driver == null) {
            driver = new AltDriver(
                    TestConfig.host(),
                    TestConfig.port(),
                    TestConfig.enableLogging(),
                    TestConfig.connectTimeoutSec(),
                    TestConfig.appName());
        }
        return driver;
    }

    public static synchronized void quit() {
        if (driver == null) {
            return;
        }
        try {
            driver.stop();
        } catch (IOException e) {
            System.err.println("Failed to stop AltDriver cleanly: " + e.getMessage());
        } finally {
            driver = null;
        }
    }
}
