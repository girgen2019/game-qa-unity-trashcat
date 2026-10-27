package io.github.girgen2019.trashcat.pages;

import com.alttester.AltDriver;

/**
 * Missions popup of the main menu: an instance of Prefabs/UI/MissionPopup.prefab under UICamera/Loadout.
 * The Game Over screen has its own MissionPopup instance, so the locators start from //Loadout.
 * Inactive until MissionButton calls MissionUI.CallOpen(); CloseButton calls MissionUI.Close().
 */
public class MissionsPopup extends BasePage {

    private static final String ROOT = "//Loadout/MissionPopup/MissionBackground/";

    public MissionsPopup(AltDriver driver) {
        super(driver);
    }

    @Override
    public boolean isDisplayed() {
        return byPath(ROOT + "Text") != null
                && byPath(ROOT + "MissionsContainer") != null
                && byPath(ROOT + "CloseButton") != null;
    }

    /** Taps the close button and waits until the popup is gone. */
    public MainMenuPage close() {
        byPath(ROOT + "CloseButton").tap();
        waitUntilGone(AltDriver.By.PATH, ROOT + "CloseButton");
        return new MainMenuPage(driver);
    }
}
