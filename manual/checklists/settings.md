# Checklist — Settings

Screen: Main menu → ⚙ Settings. Controls: **Master**, **Music**, **SFX** sliders, **About**, **Delete Data**, **X** (close).

Status: ✅ pass · ❌ fail (link to bug) · ⏭ skipped · empty = not run yet.

| ID | Check | Expected result | Status |
|---|---|---|---|
| SET-01 | Open Settings from the main menu | Panel opens over the menu, all controls visible and not cut off on 720×1640 | |
| SET-02 | Close Settings with **X** | Panel closes, main menu is interactive again | |
| SET-03 | Close Settings with the Android **Back** gesture / button | Panel closes (or nothing happens), the app does not exit or freeze | |
| SET-04 | Move **Music** slider to 0 and back to max | Music volume follows the slider smoothly, no clicks or delay | |
| SET-05 | Move **SFX** slider to 0, start a run, collect a coin | No sound effects, music still plays | |
| SET-06 | Move **Master** slider to 0 with Music and SFX at max | No sound at all in menu and in a run | |
| SET-07 | Change all three sliders → force-stop the app → launch again | All three values are restored | |
| SET-08 | Change sliders → press Home → return to the game | Values are kept, sound resumes at the set level | |
| SET-09 | Drag a slider beyond its ends | Value stays within 0…max, the handle does not leave the track | |
| SET-10 | Fast taps on different points of a slider | Value follows the last tap, no stuck handle | |
| SET-11 | Open **About** → go back | About content is readable, return to Settings works | |
| SET-12 | Tap **Delete Data** → cancel | Confirmation is shown; after cancel coins, purchases and high score are unchanged | |
| SET-13 | Tap **Delete Data** → confirm | Coins, purchases, missions and high scores are reset to defaults | |
| SET-14 | After SET-13 force-stop and relaunch | Progress is still reset (deleted data does not come back) | |
| SET-15 | Change volume sliders → Delete Data → confirm → relaunch | Volumes live in the same save file (`save.bin`) as progress: check whether they reset to defaults and that the result is the same after relaunch | |
| SET-16 | Rotate the device / change system font size with Settings open | Layout stays usable, text is not cut off | |

**Notes from the first look (Redmi A5):** on the first open Music was at 0 while Master and SFX were at max —
to clarify whether this is the default value or was changed earlier (relevant for SET-07).
