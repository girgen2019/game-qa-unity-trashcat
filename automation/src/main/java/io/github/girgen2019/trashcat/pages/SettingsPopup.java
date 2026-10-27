package io.github.girgen2019.trashcat.pages;

import com.alttester.AltDriver;

/**
 * Settings popup over the main menu (Loadout/SettingPopup in Assets/Scenes/Main.unity).
 * The popup is inactive until SettingButton is tapped; CloseButton calls SettingPopup.Close().
 */
public class SettingsPopup extends BasePage {

    private static final String ROOT = "//SettingPopup/Background/";

    public SettingsPopup(AltDriver driver) {
        super(driver);
    }

    @Override
    public boolean isDisplayed() {
        return byPath(ROOT + "MasterSlider") != null
                && byPath(ROOT + "MusicSlider") != null
                && byPath(ROOT + "MasterSFXSlider") != null
                && byPath(ROOT + "DeleteData") != null
                && byPath(ROOT + "CloseButton") != null;
    }

    /** Taps the close button and waits until the popup is gone. */
    public MainMenuPage close() {
        byPath(ROOT + "CloseButton").tap();
        waitUntilGone(AltDriver.By.PATH, ROOT + "CloseButton");
        return new MainMenuPage(driver);
    }
}
