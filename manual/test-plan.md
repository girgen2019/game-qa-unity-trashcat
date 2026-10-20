# Test plan — TrashCat (Android)

## 1. Product

TrashCat is a 3D endless runner built with Unity: the cat runs along three lanes, the player swipes to change
lanes, jump and slide, collects fish (coins) and power-ups and avoids obstacles. Coins are spent in the Store.

| Area | What is in the build |
|---|---|
| Start screen | START button; AltTester® connection popup (development build only) |
| Main menu | Run!, theme selector (Day / Night), Leaderboard, Store, Missions, Settings |
| Gameplay | 3 lanes, jump / slide, coins, power-ups, obstacles, pause, game over, "get another chance" |
| Store | Tabs: Items (consumables), Characters, Accessories, Themes — bought with coins |
| Settings | Master / Music / SFX volume sliders, About, Delete Data |
| Missions | Collect coins, jump barriers, slide, reach a multiplier, single-run goals |
| Leaderboard | Local high scores (no network calls in the build) |
| Persistence | Coins, purchases, high scores, missions progress, settings |

## 2. Scope

**In scope:** functional and gameplay testing, UI, settings and save data, interruptions, install / update /
cold start, stability (crashes, ANR), basic performance on a low-end device.

**Out of scope:** real payments and ads (Unity Ads / IAP are removed from this build), backend,
localization (no localization in the build), iOS.

## 3. Environment

| | |
|---|---|
| Device | Redmi A5 (25028RN03A), Android 15, 720×1640, 320 dpi — low-end device |
| Build | TrashCat 1.0 (build 350), `com.unity.trashdash`, arm64-v8a, Development Build with AltTester® SDK 2.3.0 |
| Tools | ADB / logcat, screen recording, scrcpy, Android Studio Logcat |

## 4. Test types and approach

| Type | Approach |
|---|---|
| Smoke | Install → cold start → main menu → one run → game over. Automated part: `SMK-01..03` (ADB) |
| Functional | Checklists per area (main menu, gameplay, store, settings, missions) |
| Gameplay | Controls responsiveness, collisions, power-up effects and duration, score and coin counting |
| Save data | Values survive force-stop and relaunch; Delete Data resets progress and asks for confirmation |
| Interruptions | Home / recent apps, screen lock, incoming notification, network off / on, low battery dialog |
| Compatibility | Portrait layout on 20:9 screen, system font size, dark mode, gesture vs 3-button navigation |
| Stability | Long session (15+ min), repeated restarts, crash / ANR markers in logcat |
| Performance | Visual FPS drops, loading time from cold start, device heating on a 15 min session |
| Exploratory | Time-boxed sessions with a charter, notes in `sessions/` |

## 5. Entry and exit criteria

**Entry:** build installs and starts; main menu is reachable.

**Exit:** all checklists executed; no open Blocker / Critical bugs; Major bugs reported with repro steps,
video and logcat.

## 6. Bug reporting

Every bug report contains: title `[Area] short description`, environment (device, Android, build), preconditions,
steps, expected / actual result, severity, frequency (e.g. 3/5), attachments (video or screenshot, `adb logcat`).

Severity scale: Blocker (cannot play / crash on start) · Critical (crash or progress loss) · Major (feature broken,
workaround exists) · Minor (UI / text issue) · Trivial (cosmetic).

## 7. Risks

| Risk | Mitigation |
|---|---|
| Low-end device: performance problems may hide functional ones | Note FPS drops separately, repeat on emulator when needed |
| Development build shows the AltTester® popup over the start screen | Close it before UI checks; not a product bug |
| Randomly generated track makes bugs hard to reproduce | Record video for every run, write down seed-related context (theme, character, power-up) |

## 8. Deliverables

`test-plan.md` (this file) · `checklists/` · `bug-reports/` · `sessions/` · automated smoke results in the
Allure report (`automation/`).
