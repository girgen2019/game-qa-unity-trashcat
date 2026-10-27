# 🎮 Game QA: Unity · TrashCat

[![Build](https://github.com/girgen2019/game-qa-unity-trashcat/actions/workflows/build.yml/badge.svg)](https://github.com/girgen2019/game-qa-unity-trashcat/actions/workflows/build.yml)

Game QA project for **TrashCat** — an endless-runner mobile/PC game built with **Unity**
(the AltTester fork of Unity's *Endless Runner* sample: [alttester/trashcat](https://github.com/alttester/trashcat)).

The project combines two parts:

| Part | What's inside | Status |
|---|---|---|
| [🧪 Manual testing](manual/) | Test plan, checklists, bug reports and exploratory session notes from real devices | 🚧 In progress |
| [🤖 Test automation](automation/) | Java framework: ADB smoke tests on a real device ✅, AltTester® Page Objects for game screens 🚧, JUnit 5, Allure reports, CI | 🚧 In progress |

## 🛠 Stack

`Unity 2021.3 LTS` `AltTester® Unity SDK 2.3` `Java 17` `Maven` `JUnit 5` `AssertJ` `Allure` `GitHub Actions` `Android (ADB)` `Windows`

## 🧩 How it works

```
Java tests (JUnit 5)  ──►  AltTester® Desktop (PC)  ◄──  TrashCat build with AltTester SDK
                                                          (Android via USB / Windows)
```

The game is built with the AltTester® Unity SDK, which lets tests find real game objects
(buttons, the character, coins) inside the running scene instead of relying on screen pixels.

## 📱 Test environment

| | |
|---|---|
| Android device | Redmi A5 (25028RN03A), Android 15, 720×1640 |
| Game build | TrashCat 1.0 (build 350), arm64-v8a, Development Build with AltTester® SDK 2.3.0 |
| PC | Windows 11 |

## ✅ What is tested now

| ID | Test | Level | Runs in CI |
|---|---|---|---|
| SMK-01 | Game is installed on the device | Android device (ADB) | ❌ needs a phone |
| SMK-02 | Cold start: game comes to the foreground, process is running | Android device (ADB) | ❌ needs a phone |
| SMK-03 | Game keeps running 30 s: same process, no crash / ANR / unhandled Unity exception in logcat | Android device (ADB) | ❌ needs a phone |
| SMK-05 | Restart: force-stop the running game and launch it again — on top, new process, no crash *(written, not run yet)* | Android device (ADB) | ❌ needs a phone |
| PERF-01 | Cold start (`am start -W` TotalTime) below `android.maxColdStartMs` (5000 ms) *(written, not run yet)* | Android device (ADB) | ❌ needs a phone |
| PERF-02 | Memory (`dumpsys meminfo` TOTAL PSS) after 30 s below `android.maxMemoryMb` (600 MB) *(written, not run yet)* | Android device (ADB) | ❌ needs a phone |
| ALT-01 | In-game (AltTester®): START opens the main menu with a ready Run button *(written, not run yet)* | Game + AltTester Desktop | ❌ needs the game |
| ALT-02 | In-game: Run! starts a run — the cat on screen, 3 lives *(written, not run yet)* | Game + AltTester Desktop | ❌ needs the game |
| ALT-03 | In-game: Pause → Resume keeps the run and the lives *(written, not run yet)* | Game + AltTester Desktop | ❌ needs the game |
| ALT-04 | In-game: Pause → Exit returns to the main menu *(written, not run yet)* | Game + AltTester Desktop | ❌ needs the game |
| ALT-05 | In-game: Store opens from the main menu with all four tabs and the currency counters *(written, not run yet)* | Game + AltTester Desktop | ❌ needs the game |
| ALT-06 | In-game: distance in the HUD grows while the cat runs (read twice, 2 s apart) *(written, not run yet)* | Game + AltTester Desktop | ❌ needs the game |
| ALT-07 | In-game: Settings popup opens (3 volume sliders, Delete Data, Close) and closes back to the menu *(written, not run yet)* | Game + AltTester Desktop | ❌ needs the game |
| ALT-08 | In-game: Leaderboard opens (title, score table, close) and closes back to the menu *(written, not run yet)* | Game + AltTester Desktop | ❌ needs the game |
| ALT-09 | In-game: Missions popup opens (title, missions list, close) and closes back to the menu *(written, not run yet)* | Game + AltTester Desktop | ❌ needs the game |
| — | `CrashDetector` unit tests on real logcat lines (incl. Xiaomi system noise) | Unit | ✅ |
| — | `TestConfig` unit tests: defaults from `config.properties`, `-D` overrides, trimming | Unit | ✅ |
| — | `AdbOutput` unit tests: parsing `am start -W` and `dumpsys meminfo` output (real samples) | Unit | ✅ |

Each device test attaches screenshots and the logcat dump to the Allure report.

## 🚀 Running the smoke tests on an Android device

Needs only JDK 17+ and Android platform-tools (`adb`) — no AltTester Desktop.

1. Enable **USB debugging** on the phone, connect it and check it is visible:
   ```bash
   adb devices
   ```
2. Install the game build (see [How it works](#-how-it-works)) — package `com.unity.trashdash`.
3. Run the tests and open the report:
   ```bash
   cd automation
   ./mvnw clean test
   ./mvnw allure:serve
   ```
   If `adb` is not on `PATH`, pass its location:
   `./mvnw clean test "-Dadb.path=C:/Users/<you>/AppData/Local/Android/Sdk/platform-tools/adb.exe"`

Without a connected device the smoke tests are **skipped**, not failed.

## 🚀 Running the AltTester® tests (next step)

1. Build TrashCat with the AltTester® Unity SDK (Android or Windows) and install/launch it.
2. Start **AltTester® Desktop 2.3.x** on the PC.
3. For Android over USB forward the port:
   ```bash
   adb reverse tcp:13000 tcp:13000
   ```
4. Run the tests:
   ```bash
   cd automation
   ./mvnw test
   ```

Connection settings live in [`automation/src/main/resources/config.properties`](automation/src/main/resources/config.properties)
and can be overridden with `-D`, e.g. `./mvnw test -Dalttester.port=13001`.

## 🗺 Roadmap

- [x] Instrumented Android build running on a real device
- [x] Framework skeleton: config, driver lifecycle, base Page Object, CI build
- [x] ADB smoke tests on a real device (install, cold start, 30 s stability, crash detection) with Allure attachments
- [ ] Manual: test plan and checklists (gameplay, UI, interruptions, PC-specific checks)
- [ ] Manual: bug reports with video and `adb logcat`
- [x] Automation: Page Objects for Start, Main menu, Game, Pause (names checked against the game scenes)
- [x] Automation: Page Object for the Shop (store screen, tabs, counters)
- [ ] Automation: smoke tests (start game, run, pause/resume, game over, shop)
- [ ] Allure report published from CI
- [ ] Windows build and PC-specific checks
