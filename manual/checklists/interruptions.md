# Checklist — Interruptions

Mobile games must survive the player leaving and coming back. TrashCat pauses itself when the app
loses focus or goes to the background (`GameState.OnApplicationPause` / `OnApplicationFocus`), so after
every interruption **during a run** the expected state is: **pause menu, same run, no life lost, same process**.

Run each check **during a run** unless stated otherwise. The "How to trigger" column gives an ADB command
so the check is repeatable; doing the same by hand on the phone is equally valid.
All commands below are available on the Redmi A5 (Android 15).

Status: ✅ pass · ❌ fail (link to bug) · ⏭ skipped · empty = not run yet.

| ID | Interruption | How to trigger | Expected result | Status |
|---|---|---|---|---|
| INT-01 | Home button | `adb shell input keyevent KEYCODE_HOME` | Game goes to background; on return the run is paused | |
| INT-02 | Recent apps → back to the game | `adb shell input keyevent KEYCODE_APP_SWITCH` | Run is paused, resume works | |
| INT-03 | Switch to another app for 1 minute | Open any app, wait, return via recent apps | Same process (`adb shell pidof com.unity.trashdash` unchanged), run paused | |
| INT-04 | Screen lock / unlock | `adb shell input keyevent KEYCODE_POWER` (twice) | Run is paused after unlock, no audio while locked | |
| INT-05 | Notification arrives | `adb shell cmd notification post -S bigtext qa "Test notification"` | Heads-up banner over the game; run continues or pauses, no input lost after it is dismissed | |
| INT-06 | Pull down the notification shade | Swipe down from the top edge | Game loses focus → run is paused | |
| INT-07 | Wi-Fi off / on | `adb shell svc wifi disable` → `enable` | Offline game: no errors, run and menus keep working | |
| INT-08 | Airplane mode on / off | `adb shell cmd connectivity airplane-mode enable` → `disable` | Same as INT-07 | |
| INT-09 | Low battery | `adb shell cmd battery unplug` → `adb shell cmd battery set level 4` → `adb shell cmd battery reset` | Low battery dialog over the game; run is paused, resume works | |
| INT-10 | Charger connected / disconnected | `adb shell cmd battery set ac 1` → `reset` | No pause, no stutter | |
| INT-11 | Volume buttons during a run | Hardware volume keys | Volume changes, run is not paused | |
| INT-12 | Interruption in the main menu / Store / Settings | INT-01 on each screen | Same screen is shown after return, no reset to the start screen | |
| INT-13 | Interruption during the countdown | INT-01 right after **Run!** | Countdown restarts or the run is paused — no run starting in the background | |
| INT-14 | Interruption on the game over screen | INT-01 on game over | Game over screen is still shown; "another chance" is not lost or charged twice | |
| INT-15 | App killed in the background | INT-01, then `adb shell am kill com.unity.trashdash`, relaunch | Cold start to the start screen; coins and high score from finished runs are kept | |

**After the session:** restore the device — Wi-Fi on, airplane mode off, `adb shell cmd battery reset`.
Attach `adb logcat -d` to any failed check.
