# 🧪 Manual testing

Manual testing of TrashCat on a real Android device and on Windows.
Artifacts are added here only after real test runs — no placeholders.

## Environment

| | |
|---|---|
| Device | Redmi A5 (25028RN03A), Android 15, 720×1640, 320 dpi |
| Build | TrashCat 1.0 (build 350), `com.unity.trashdash`, arm64-v8a, Development Build with AltTester® SDK 2.3.0 |
| Tools | ADB / logcat, scrcpy, screen recording |

## Planned structure

| Folder | Content |
|---|---|
| [`test-plan.md`](test-plan.md) | Scope, test types, entry/exit criteria, bug report format ✅ |
| [`checklists/`](checklists/) | Settings ✅ · Gameplay ✅ · Interruptions ✅ · Store, PC-specific checks 🚧 |
| `bug-reports/` | Bugs found: steps, expected/actual, severity, video/screenshots, logcat |
| `sessions/` | Exploratory testing session reports |
