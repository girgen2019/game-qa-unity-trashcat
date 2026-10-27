package io.github.girgen2019.trashcat.game;

import io.github.girgen2019.trashcat.BaseTest;
import io.github.girgen2019.trashcat.pages.GamePlayPage;
import io.github.girgen2019.trashcat.pages.MainMenuPage;
import io.github.girgen2019.trashcat.pages.PauseMenuPage;
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
}
