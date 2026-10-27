package io.github.girgen2019.trashcat.pages;

import com.alttester.AltDriver;
import io.github.girgen2019.trashcat.config.TestConfig;

/**
 * Main menu (scene "Main"): Run!, Store, Missions, Leaderboard, Settings, character and theme.
 * Object names are taken from the game scene Assets/Scenes/Main.unity.
 */
public class MainMenuPage extends BasePage {

    // The Run button shows "Loading…" while the character and theme load, then "Run!"
    private static final String RUN_READY_TEXT = "Run!";

    public MainMenuPage(AltDriver driver) {
        super(driver);
    }

    @Override
    public boolean isDisplayed() {
        return byPath("//StoreButton") != null
                && byPath("//Loadout/MissionButton") != null
                && byPath("//Loadout/OpenLeaderboard") != null
                && byPath("//SettingButton") != null
                && byPath("//StartButton/Text") != null;
    }

    public String runButtonText() {
        return byPath("//StartButton/Text").getText();
    }

    public String characterName() {
        return byPath("//CharZone/CharName").getText();
    }

    /** The menu's own leaderboard button; the Game Over screen has another one with the same name. */
    public LeaderboardPopup pressLeaderboard() {
        byPath("//Loadout/OpenLeaderboard").tap();
        return new LeaderboardPopup(driver);
    }

    public MissionsPopup pressMissions() {
        byPath("//Loadout/MissionButton").tap();
        return new MissionsPopup(driver);
    }

    public SettingsPopup pressSettings() {
        byPath("//SettingButton").tap();
        return new SettingsPopup(driver);
    }

    public ShopPage pressStore() {
        byPath("//StoreButton").tap();
        return new ShopPage(driver);
    }

    /** Waits until the Run button is ready (text "Run!"), then taps it. */
    public GamePlayPage pressRun() throws InterruptedException {
        long deadline = System.currentTimeMillis() + (long) (TestConfig.waitTimeoutSec() * 1000);
        while (!RUN_READY_TEXT.equals(runButtonText())) {
            if (System.currentTimeMillis() > deadline) {
                throw new IllegalStateException("Run button is not ready, text: " + runButtonText());
            }
            Thread.sleep(200);
        }
        byPath("//StartButton").tap();
        return new GamePlayPage(driver);
    }
}
