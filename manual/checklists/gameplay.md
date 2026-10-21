# Checklist — Gameplay

Scope: one run from **Run!** to game over. Facts below are taken from the game sources
(`CharacterInputController`, `GameState`, `Consumable/Types`).

Status: ✅ pass · ❌ fail (link to bug) · ⏭ skipped · empty = not run yet.

## Controls and movement

| ID | Check | Expected result | Status |
|---|---|---|---|
| GP-01 | Tap **Run!** | Countdown, then the cat starts running in the middle lane | |
| GP-02 | Swipe left / right | Cat moves one lane per swipe; no move beyond the left or right lane | |
| GP-03 | Swipe up | Cat jumps over a low barrier | |
| GP-04 | Swipe down | Cat slides under a high barrier | |
| GP-05 | Swipe down while in the air | Cat drops and slides (or the input is ignored) — behaviour is consistent | |
| GP-06 | Fast swipes left-right-left | Every swipe is handled, the cat never stops between lanes | |
| GP-07 | Short tap without swipe | No movement, no jump | |

## Lives, collisions, game over

| ID | Check | Expected result | Status |
|---|---|---|---|
| GP-08 | Start of a run | **3 lives** shown in the HUD | |
| GP-09 | Hit an obstacle | One life lost, short invulnerability, run continues | |
| GP-10 | Lose all 3 lives | Game over screen with score, coins and distance | |
| GP-11 | Game over with **≥ 3 premium** | "Get another chance" for 3 premium is available; after it the run continues and 3 premium are deducted | |
| GP-12 | Game over with **< 3 premium** | Premium button is disabled, nothing is deducted | |
| GP-13 | Finish a run with a new best score | High score is saved and shown in the Leaderboard | |
| GP-14 | Coins collected in a run | Total coins in the main menu increase by the same amount | |

## Power-ups (consumables)

| ID | Power-up | Store price | Check | Expected result | Status |
|---|---|---|---|---|---|
| GP-15 | Coin Magnet | 750 | Pick up / activate | Coins from nearby lanes fly to the cat until the timer ends | |
| GP-16 | Score ×2 | 750 | Pick up / activate | Score grows twice as fast while active; multiplier returns after the timer | |
| GP-17 | Invincibility | 1500 | Hit an obstacle while active | No life lost; effect ends with the timer | |
| GP-18 | Extra Life | 2000 | Use with 2 lives | Lives become 3 | |
| GP-19 | Extra Life | 2000 | Try to use with full lives | Cannot be used (by design `CanBeUsed` is false at max lives) | |
| GP-20 | Extra Life | — | Pick up on the track with full lives | **10 coins** are added instead of a life (`k_CoinValue = 10`) | |
| GP-21 | Two power-ups at once | — | Magnet + ×2 | Both timers are shown and both effects work | |

## Pause and HUD

| ID | Check | Expected result | Status |
|---|---|---|---|
| GP-22 | Tap pause | Run freezes, pause menu with resume / main menu | |
| GP-23 | Resume | Run continues from the same place, no life lost | |
| GP-24 | Pause → main menu | Run ends, coins from the run are kept or lost consistently (note which) | |
| GP-25 | HUD during a run | Score, coins, lives, power-up timers are readable and not overlapped on 720×1640 | |

## Themes and characters

| ID | Check | Expected result | Status |
|---|---|---|---|
| GP-26 | Switch theme Day → Night (if unlocked) and run | Night track, sky and obstacles load, no missing textures | |
| GP-27 | Change character in the Store and run | Selected character is used in the run and in the menu | |
