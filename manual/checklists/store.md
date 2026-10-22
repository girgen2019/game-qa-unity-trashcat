# Checklist — Store

Screen: Main menu → 🛒 Store. Tabs: **Items** (consumables), **Characters**, **Accessories**, **Themes**.
Two currencies: **coins** (fishbones) and **premium**. Rules below are taken from the game sources
(`UI/Shop/ShopUI`, `ShopItemList`, `ShopCharacterList`, `ShopThemeList`, `ShopAccessoriesList`).

**Test data:** the development build has a cheat button in the Store that adds **1 000 000 coins and
1000 premium** (`ShopUI.CheatCoin`). Use it to prepare currency; use **Settings → Delete Data** to go back
to a fresh player.

Status: ✅ pass · ❌ fail (link to bug) · ⏭ skipped · empty = not run yet.

## Navigation and balance

| ID | Check | Expected result | Status |
|---|---|---|---|
| ST-01 | Open the Store from the main menu | Store opens on the first tab, coin and premium counters match the main menu | |
| ST-02 | Switch between all four tabs | Each tab shows its list, no empty or overlapping items on 720×1640 | |
| ST-03 | Close the Store | Back to the main menu, counters there show the same balance | |

## Buying with enough and not enough currency

| ID | Check | Expected result | Status |
|---|---|---|---|
| ST-04 | Fresh player (Delete Data) → Items tab | Items priced above the balance have a **disabled Buy button** and the **price in red** | |
| ST-05 | Buy an item with exactly enough coins | Purchase succeeds, balance becomes 0, no negative balance | |
| ST-06 | Buy a consumable (e.g. Coin Magnet, 750) | Coins −750, item counter +1, counters update immediately | |
| ST-07 | Buy the same consumable several times | Counter keeps growing (consumables stack), each purchase charges the price again | |
| ST-08 | Item that costs premium with not enough premium | Buy is disabled, premium price is red | |
| ST-09 | Fast double tap on Buy | Exactly one purchase, currency charged once | |

## Characters, accessories, themes

| ID | Check | Expected result | Status |
|---|---|---|---|
| ST-10 | Buy a character | Currency charged, button changes to **Owned** and becomes disabled | |
| ST-11 | Select the bought character and start a run | The character is used in the run (see GP-27) | |
| ST-12 | Accessories tab: buy an accessory for a character you do **not** own | Expected: not possible, or clearly explained. ⚠️ Source check: the tab lists accessories of **all** characters and the Buy button only checks balance and "already owned", not character ownership — likely bug / design question, verify on device | |
| ST-13 | Buy an accessory for an owned character | Button becomes **Owned**; accessory can be equipped | |
| ST-14 | Buy the Night theme | Button becomes **Owned**; Night theme becomes selectable in the main menu (see GP-26) | |

## Persistence

| ID | Check | Expected result | Status |
|---|---|---|---|
| ST-15 | Buy something → force-stop → relaunch | Purchase and the new balance are kept (saved to `save.bin` right after buying) | |
| ST-16 | Buy something → Settings → Delete Data | Purchases are gone, balance is back to the starting value (0 coins, 0 premium) | |
| ST-17 | Use a bought consumable in a run | Counter in the Store decreases by one after the run | |

## Build-specific checks

| ID | Check | Expected result | Status |
|---|---|---|---|
| ST-18 | Cheat button in a **development** build | Adds 1 000 000 coins and 1000 premium, counters update | |
| ST-19 | Cheat button in a **release** build | Button is disabled and does nothing (`!DEVELOPMENT_BUILD`) — **must be checked before every release** | |
| ST-20 | "Watch ad for coins" button (if shown) | Ads are removed from this build: the button is hidden or disabled, tapping it does not freeze the Store and does not add coins | |
