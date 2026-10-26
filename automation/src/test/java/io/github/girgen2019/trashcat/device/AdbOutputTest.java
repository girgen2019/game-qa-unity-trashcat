package io.github.girgen2019.trashcat.device;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for parsing adb output. Samples are real output from TrashCat on a Redmi A5 (Android 15).
 * No device needed, so they run in CI.
 */
@Feature("ADB output parsing")
@DisplayName("AdbOutput")
class AdbOutputTest {

    private static final String AM_START = String.join("\n",
            "Starting: Intent { cmp=com.unity.trashdash/com.unity3d.player.UnityPlayerActivity }",
            "Status: ok",
            "LaunchState: COLD",
            "Activity: com.unity.trashdash/com.unity3d.player.UnityPlayerActivity",
            "TotalTime: 715",
            "WaitTime: 719",
            "Complete");

    private static final String MEMINFO = String.join("\n",
            "Applications Memory Usage (in Kilobytes):",
            "** MEMINFO in pid 30315 [com.unity.trashdash] **",
            "                 Total    Dirty    Clean    Dirty    Total     Size    Alloc     Free",
            "        TOTAL   379420   292304    68748      317   487296    66806    58214     8591",
            "           TOTAL PSS:   379420            TOTAL RSS:   487296       TOTAL SWAP PSS:      317");

    @Test
    @DisplayName("Reads TotalTime, not WaitTime, from 'am start -W'")
    void readsLaunchTime() {
        assertThat(AdbOutput.launchTotalTimeMs(AM_START)).isEqualTo(715);
    }

    @Test
    @DisplayName("Reads TOTAL PSS in kilobytes from 'dumpsys meminfo'")
    void readsTotalPss() {
        assertThat(AdbOutput.totalPssKb(MEMINFO)).isEqualTo(379420);
    }

    @Test
    @DisplayName("Fails with a clear message when the value is missing")
    void failsWhenValueMissing() {
        assertThatThrownBy(() -> AdbOutput.launchTotalTimeMs("Error: Activity not started"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("TotalTime not found");
    }
}
