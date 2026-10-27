package io.github.girgen2019.trashcat.pages;

import com.alttester.AltDriver;
import com.alttester.Commands.UnityCommand.AltLoadSceneParams;

/**
 * Start screen (scene "Start"): logo and the START button.
 * Object names are taken from the game scene Assets/Scenes/Start.unity.
 */
public class StartPage extends BasePage {

    public StartPage(AltDriver driver) {
        super(driver);
    }

    /** Opens the start screen from any state, so every test begins the same way. */
    public StartPage load() {
        driver.loadScene(new AltLoadSceneParams.Builder("Start").build());
        return this;
    }

    @Override
    public boolean isDisplayed() {
        return byName("StartButton") != null && byName("StartText") != null && byName("LogoImage") != null;
    }

    public MainMenuPage pressStart() {
        byName("StartButton").tap();
        return new MainMenuPage(driver);
    }
}
