package io.github.girgen2019.trashcat.game;

import io.github.girgen2019.trashcat.BaseTest;
import io.github.girgen2019.trashcat.pages.GamePlayPage;
import io.github.girgen2019.trashcat.pages.LeaderboardPopup;
import io.github.girgen2019.trashcat.pages.MainMenuPage;
import io.github.girgen2019.trashcat.pages.PauseMenuPage;
import io.github.girgen2019.trashcat.pages.SettingsPopup;
import io.github.girgen2019.trashcat.pages.ShopPage;
import io.github.girgen2019.trashcat.pages.StartPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * In-game flow through AltTester®: start screen → main menu → run → pause → resume / exit.
 * Every test starts from the Start scene, so the tests are independent of each other.
 */
@Feature("Game flow (AltTester)")
@DisplayName("Game flow: start, run, pause")
class GameFlowTest extends BaseTest {

    private StartPage start;

    @BeforeEach
    void openStartScreen() {
        start = Allure.step("Open the start screen", () -> new StartPage(driver).load());
    }

    @Test
    @DisplayName("ALT-01 START opens the main menu with a ready Run button")
    void startOpensMainMenu() {
        assertThat(start.isDisplayed()).as("Start screen: START button, text and logo").isTrue();

        MainMenuPage menu = Allure.step("Tap START", start::pressStart);

        assertThat(menu.isDisplayed()).as("Main menu: Store, Missions, Leaderboard, Settings, Run").isTrue();
        assertThat(menu.currentScene()).isEqualTo("Main");
    }

    @Test
    @DisplayName("ALT-02 Run starts a run with the cat and 3 lives")
    @Description("A new run begins with CharacterInputController.maxLife = 3 lives")
    void runStartsWithThreeLives() throws InterruptedException {
        MainMenuPage menu = start.pressStart();

        GamePlayPage game = Allure.step("Tap Run!", menu::pressRun);

        assertThat(game.isDisplayed()).as("Run: the cat and the pause button are on screen").isTrue();
        assertThat(game.currentLife()).as("Lives at the start of a run").isEqualTo(3);
    }

    @Test
    @DisplayName("ALT-03 Pause and Resume keep the run and the lives")
    void pauseAndResumeKeepTheRun() throws InterruptedException {
        GamePlayPage game = start.pressStart().pressRun();
        int livesBefore = game.currentLife();

        PauseMenuPage pause = Allure.step("Tap pause", game::pressPause);
        assertThat(pause.isDisplayed()).as("Pause menu: Resume and Exit").isTrue();

        GamePlayPage resumed = Allure.step("Tap Resume", pause::pressResume);
        assertThat(resumed.isDisplayed()).as("Back in the run after Resume").isTrue();
        assertThat(resumed.currentLife()).as("Lives after pause/resume").isEqualTo(livesBefore);
    }

    @Test
    @DisplayName("ALT-04 Exit from the pause menu returns to the main menu")
    void exitFromPauseReturnsToMainMenu() throws InterruptedException {
        GamePlayPage game = start.pressStart().pressRun();

        MainMenuPage menu = Allure.step("Pause and tap Exit", () -> game.pressPause().pressExit());

        assertThat(menu.isDisplayed()).as("Main menu after leaving the run").isTrue();
    }

    @Test
    @DisplayName("ALT-06 Distance in the HUD grows while the cat runs")
    @Description("Reads the HUD distance twice, 2 s apart: the run must actually move forward")
    void distanceGrowsWhileRunning() throws InterruptedException {
        GamePlayPage game = start.pressStart().pressRun();

        int first = game.distanceMeters();
        Allure.step("Let the cat run for 2 s");
        Thread.sleep(2000);
        int second = game.distanceMeters();

        Allure.addAttachment("Distance", "text/plain", first + " m -> " + second + " m", ".txt");
        assertThat(second).as("Distance after 2 s (was %d m)", first).isGreaterThan(first);
    }

    @Test
    @DisplayName("ALT-07 Settings popup opens and closes")
    @Description("Settings shows the three volume sliders, Delete Data and Close; Close hides the popup "
            + "and the main menu is usable again")
    void settingsOpensAndCloses() {
        MainMenuPage menu = start.pressStart();

        SettingsPopup settings = Allure.step("Tap Settings", menu::pressSettings);
        assertThat(settings.isDisplayed()).as("Settings: Master, Music, SFX sliders, Delete Data, Close").isTrue();

        MainMenuPage back = Allure.step("Tap Close", settings::close);
        assertThat(back.isDisplayed()).as("Main menu after closing Settings").isTrue();
    }

    @Test
    @DisplayName("ALT-08 Leaderboard opens and closes")
    @Description("The menu's leaderboard button opens the high score table; its close button hides it "
            + "and the main menu is usable again")
    void leaderboardOpensAndCloses() {
        MainMenuPage menu = start.pressStart();

        LeaderboardPopup leaderboard = Allure.step("Tap Leaderboard", menu::pressLeaderboard);
        assertThat(leaderboard.isDisplayed()).as("Leaderboard: title, score table, close button").isTrue();

        MainMenuPage back = Allure.step("Tap Close", leaderboard::close);
        assertThat(back.isDisplayed()).as("Main menu after closing the Leaderboard").isTrue();
    }

    @Test
    @DisplayName("ALT-05 Store opens from the main menu with all four tabs")
    void storeOpensWithAllTabs() {
        MainMenuPage menu = start.pressStart();

        ShopPage shop = Allure.step("Tap Store", menu::pressStore);

        assertThat(shop.isDisplayed()).as("Store: title and tabs Item, Character, Accessories, Themes").isTrue();
        assertThat(shop.currencyCountersShown()).as("Store: coins and premium counters").isTrue();
    }
}
