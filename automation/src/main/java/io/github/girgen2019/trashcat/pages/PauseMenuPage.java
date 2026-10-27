package io.github.girgen2019.trashcat.pages;

import com.alttester.AltDriver;

/**
 * Pause menu over a run: Resume and Exit (back to the main menu).
 */
public class PauseMenuPage extends BasePage {

    public PauseMenuPage(AltDriver driver) {
        super(driver);
    }

    @Override
    public boolean isDisplayed() {
        return byPath("//PauseMenu/Resume") != null && byPath("//PauseMenu/Exit") != null;
    }

    public GamePlayPage pressResume() {
        byPath("//PauseMenu/Resume").tap();
        waitUntilGone(AltDriver.By.PATH, "//PauseMenu/Resume");
        return new GamePlayPage(driver);
    }

    public MainMenuPage pressExit() {
        byPath("//PauseMenu/Exit").tap();
        return new MainMenuPage(driver);
    }
}
