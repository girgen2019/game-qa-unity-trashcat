package io.github.girgen2019.trashcat.device;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the crash filter. No device needed, so they also run in CI.
 * Sample lines are taken from a real logcat of TrashCat on a Redmi A5 (Android 15).
 */
@Feature("Crash detection")
@DisplayName("CrashDetector")
class CrashDetectorTest {

    private static final String PKG = "com.unity.trashdash";

    @Test
    @DisplayName("Ignores Xiaomi system noise that mentions the game package")
    void ignoresSystemNoise() {
        String log = String.join("\n",
                "E/UnisocWatchdog( 1109): collect3rdApps: get a 3rd :com.unity.trashdash,pid:0",
                "E/CompositionEngine(  593): yyyy: Layer Name== ",
                "SurfaceView[com.unity.trashdash/com.unity3d.player.UnityPlayerActivity](BLAST)#1042",
                "I/Unity   (26976): AltTester(R) logs are saved at: /storage/emulated/0/Android/data/com.unity.trashdash/files");

        assertThat(CrashDetector.findCrashLines(log, PKG)).isEmpty();
    }

    @Test
    @DisplayName("Detects a Java crash")
    void detectsJavaCrash() {
        String log = "E/AndroidRuntime(26976): FATAL EXCEPTION: main";

        assertThat(CrashDetector.findCrashLines(log, PKG)).hasSize(1);
    }

    @Test
    @DisplayName("Detects a native (IL2CPP) crash")
    void detectsNativeCrash() {
        String log = "F/libc    (26976): Fatal signal 11 (SIGSEGV), code 1 (SEGV_MAPERR), fault addr 0x0";

        assertThat(CrashDetector.findCrashLines(log, PKG)).hasSize(1);
    }

    @Test
    @DisplayName("Detects ANR of the game, but not of another app")
    void detectsAnrOfGameOnly() {
        String log = String.join("\n",
                "E/ActivityManager( 1109): ANR in com.unity.trashdash (com.unity.trashdash/com.unity3d.player.UnityPlayerActivity)",
                "E/ActivityManager( 1109): ANR in com.android.chrome");

        assertThat(CrashDetector.findCrashLines(log, PKG))
                .singleElement()
                .asString()
                .contains(PKG);
    }

    @Test
    @DisplayName("Detects an unhandled Unity exception")
    void detectsUnityException() {
        String log = "E/Unity   (26976): NullReferenceException: Object reference not set to an instance of an object";

        assertThat(CrashDetector.findCrashLines(log, PKG)).hasSize(1);
    }

    @Test
    @DisplayName("Ignores Unity info lines that only mention 'Exception' in text")
    void ignoresUnityInfoLines() {
        String log = "I/Unity   (26976): Loaded ExceptionHandler settings";

        assertThat(CrashDetector.findCrashLines(log, PKG)).isEmpty();
    }
}
