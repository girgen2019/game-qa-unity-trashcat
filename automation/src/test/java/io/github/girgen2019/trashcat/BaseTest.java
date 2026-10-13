package io.github.girgen2019.trashcat;

import com.alttester.AltDriver;
import io.github.girgen2019.trashcat.driver.DriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Connects to the running game once per test class and closes the connection afterwards.
 */
public abstract class BaseTest {

    protected static AltDriver driver;

    @BeforeAll
    static void connect() {
        driver = DriverManager.get();
    }

    @AfterAll
    static void disconnect() {
        DriverManager.quit();
    }
}
