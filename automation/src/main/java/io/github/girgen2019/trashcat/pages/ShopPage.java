package io.github.girgen2019.trashcat.pages;

import com.alttester.AltDriver;
import com.alttester.Commands.FindObject.AltFindObjectsParams;

/**
 * Store (scene "Shop", loaded on top of the main menu): title, four tabs and the currency counters.
 * Object names are taken from Assets/Scenes/Shop.unity. Note: the game names the accessories tab
 * "Accesories" (sic), so the locator keeps that spelling.
 */
public class ShopPage extends BasePage {

    public ShopPage(AltDriver driver) {
        super(driver);
    }

    @Override
    public boolean isDisplayed() {
        return byPath("//StoreTitle") != null
                && byPath("//TabsSwitch/Item") != null
                && byPath("//TabsSwitch/Character") != null
                && byPath("//TabsSwitch/Accesories") != null
                && byPath("//TabsSwitch/Themes") != null;
    }

    /**
     * Store tabs and the list each one shows (Canvas/Background/... in Shop.unity). ShopUI closes the
     * open list and activates the new one, so exactly one list is on screen. Items is open by default.
     */
    public enum Tab {
        ITEMS("Item", "ItemsList"),
        CHARACTERS("Character", "CharacterList"),
        ACCESSORIES("Accesories", "CharacterAccessoriesList"),
        THEMES("Themes", "ThemeList");

        final String button;
        final String list;

        Tab(String button, String list) {
            this.button = button;
            this.list = list;
        }
    }

    public void openTab(Tab tab) {
        byPath("//TabsSwitch/" + tab.button).tap();
        byPath("//Background/" + tab.list);
    }

    /** True when the tab's list is on screen (inactive lists are not found). */
    public boolean isListShown(Tab tab) {
        return driver.findObjects(new AltFindObjectsParams.Builder(AltDriver.By.PATH, "//Background/" + tab.list)
                .build()).length > 0;
    }

    public boolean currencyCountersShown() {
        return byName("CoinsCounter") != null && byName("PremiumCounter") != null;
    }

    /**
     * Taps the store's back button (Canvas/Background/Button -> ShopUI.CloseScene, which unloads the
     * Shop scene) and waits until the store is gone. Only the Shop and Start scenes have a "Canvas",
     * and Start is not loaded at this point, so the path is unambiguous.
     */
    public MainMenuPage close() {
        byPath("//Canvas/Background/Button").tap();
        waitUntilGone(AltDriver.By.PATH, "//StoreTitle");
        return new MainMenuPage(driver);
    }
}
