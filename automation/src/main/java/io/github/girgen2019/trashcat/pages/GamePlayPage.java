package io.github.girgen2019.trashcat.pages;

import com.alttester.AltDriver;
import com.alttester.Commands.ObjectCommand.AltCallComponentMethodParams;

/**
 * A run in progress: the cat (PlayerPivot), the pause button and the HUD.
 * Lives are read from the game itself: CharacterInputController.currentLife (3 at the start of a run).
 */
public class GamePlayPage extends BasePage {

    public GamePlayPage(AltDriver driver) {
        super(driver);
    }

    @Override
    public boolean isDisplayed() {
        return byPath("//pauseButton") != null && byName("PlayerPivot") != null;
    }

    public int currentLife() {
        return byName("PlayerPivot").callComponentMethod(
                new AltCallComponentMethodParams.Builder(
                        "CharacterInputController", "get_currentLife", "Assembly-CSharp", new Object[]{}).build(),
                Integer.class);
    }

    public String coinsText() {
        return byPath("//CoinZone/CoinText").getText();
    }

    public PauseMenuPage pressPause() {
        byPath("//pauseButton").tap();
        return new PauseMenuPage(driver);
    }
}
