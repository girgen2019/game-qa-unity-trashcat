package io.github.girgen2019.trashcat.device;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses the text that adb commands print. Kept separate from {@link Adb} so it can be
 * unit-tested on real output samples without a device.
 */
public final class AdbOutput {

    // "am start -W" prints e.g. "TotalTime: 715" — launch time of the activity in milliseconds
    private static final Pattern TOTAL_TIME = Pattern.compile("^TotalTime:\\s*(\\d+)", Pattern.MULTILINE);
    // "dumpsys meminfo <package>" prints e.g. "TOTAL PSS:   379420   TOTAL RSS: ..." — in kilobytes
    private static final Pattern TOTAL_PSS = Pattern.compile("TOTAL PSS:\\s*(\\d+)");

    private AdbOutput() {
    }

    public static int launchTotalTimeMs(String amStartOutput) {
        return Integer.parseInt(find(TOTAL_TIME, amStartOutput, "TotalTime"));
    }

    public static int totalPssKb(String meminfoOutput) {
        return Integer.parseInt(find(TOTAL_PSS, meminfoOutput, "TOTAL PSS"));
    }

    private static String find(Pattern pattern, String text, String what) {
        Matcher m = pattern.matcher(text);
        if (!m.find()) {
            throw new IllegalStateException(what + " not found in adb output:\n" + text);
        }
        return m.group(1);
    }
}
