package io.github.girgen2019.trashcat.pages;

import com.alttester.AltDriver;

/**
 * Leaderboard popup (UICamera/Leaderboard in Assets/Scenes/Main.unity). Inactive until opened;
 * Background/Button calls Leaderboard.Close(). Leaderboard.Open() also fills the table (Populate()).
 */
public class LeaderboardPopup extends BasePage {

    private static final String ROOT = "//Leaderboard/Background/";

    public LeaderboardPopup(AltDriver driver) {
        super(driver);
    }

    @Override
    public boolean isDisplayed() {
        return byPath(ROOT + "Text") != null
                && byPath(ROOT + "Display") != null
                && byPath(ROOT + "Button") != null;
    }

    /** Taps the close button and waits until the popup is gone. */
    public MainMenuPage close() {
        byPath(ROOT + "Button").tap();
        waitUntilGone(AltDriver.By.PATH, ROOT + "Button");
        return new MainMenuPage(driver);
    }
}
