package io.github.girgen2019.trashcat.pages;

import com.alttester.AltDriver;
import com.alttester.AltObject;
import com.alttester.Commands.FindObject.AltFindObjectsParams;
import com.alttester.Commands.FindObject.AltWaitForObjectsParams;
import io.github.girgen2019.trashcat.config.TestConfig;

/**
 * Base class for game screens (Page Object pattern applied to Unity scenes and UI panels).
 */
public abstract class BasePage {

    protected final AltDriver driver;

    protected BasePage(AltDriver driver) {
        this.driver = driver;
    }

    /** Waits until an object found by the given locator exists in the scene and returns it. */
    protected AltObject waitFor(AltDriver.By by, String value) {
        return waitFor(by, value, TestConfig.waitTimeoutSec());
    }

    protected AltObject waitFor(AltDriver.By by, String value, double timeoutSec) {
        AltFindObjectsParams find = new AltFindObjectsParams.Builder(by, value).build();
        AltWaitForObjectsParams wait = new AltWaitForObjectsParams.Builder(find)
                .withTimeout(timeoutSec)
                .build();
        return driver.waitForObject(wait);
    }

    public String currentScene() {
        return driver.getCurrentScene();
    }
}
