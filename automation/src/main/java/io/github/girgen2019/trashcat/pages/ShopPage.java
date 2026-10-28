package io.github.girgen2019.trashcat.pages;

import com.alttester.AltDriver;

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
