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

    /** Distance from the HUD; the game writes it as e.g. "123m" (GameState: worldDistance + "m"). */
    public int distanceMeters() {
        String text = byPath("//DistanceZone/DistanceText").getText();
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }

    /**
     * Distance straight from TrackManager.worldDistance. Unlike the HUD text it can be read during a pause:
     * GameState.Pause() hides the whole HUD (wholeUI), but the TrackManager object stays in the scene.
     */
    public float trackDistance() {
        return byName("TrackManager").callComponentMethod(
                new AltCallComponentMethodParams.Builder(
                        "TrackManager", "get_worldDistance", "Assembly-CSharp", new Object[]{}).build(),
                Float.class);
    }

    /** TrackManager.isMoving: false while paused (GameState.Pause() calls trackManager.StopMove()). */
    public boolean isTrackMoving() {
        return byName("TrackManager").callComponentMethod(
                new AltCallComponentMethodParams.Builder(
                        "TrackManager", "get_isMoving", "Assembly-CSharp", new Object[]{}).build(),
                Boolean.class);
    }

    public PauseMenuPage pressPause() {
        byPath("//pauseButton").tap();
        return new PauseMenuPage(driver);
    }
}
