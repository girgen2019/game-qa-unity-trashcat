# 🎮 Game QA: Unity · TrashCat

[![Build](https://github.com/girgen2019/game-qa-unity-trashcat/actions/workflows/build.yml/badge.svg)](https://github.com/girgen2019/game-qa-unity-trashcat/actions/workflows/build.yml)

Game QA project for **TrashCat** — an endless-runner mobile/PC game built with **Unity**
(the AltTester fork of Unity's *Endless Runner* sample: [alttester/trashcat](https://github.com/alttester/trashcat)).

The project combines two parts:

| Part | What's inside | Status |
|---|---|---|
| [🧪 Manual testing](manual/) | Test plan, checklists, bug reports and exploratory session notes from real devices | 🚧 In progress |
| [🤖 Test automation](automation/) | Java framework on top of **AltTester®**: Page Objects for game screens, JUnit 5 tests, Allure reports, CI | 🚧 Skeleton ready |

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

## 🚀 Running the automation

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
- [ ] Manual: test plan and checklists (gameplay, UI, interruptions, PC-specific checks)
- [ ] Manual: bug reports with video and `adb logcat`
- [ ] Automation: Page Objects for Start, Main menu, Game, Pause, Shop
- [ ] Automation: smoke tests (start game, run, pause/resume, game over, shop)
- [ ] Allure report published from CI
- [ ] Windows build and PC-specific checks
