package io.github.girgen2019.trashcat.device;

import io.github.girgen2019.trashcat.config.TestConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Thin wrapper around the adb command line tool.
 * Works without AltTester: it only talks to the Android OS, not to the game internals.
 */
public final class Adb {

    private static final long COMMAND_TIMEOUT_SEC = 60;

    private final String adbPath;

    public Adb() {
        this(TestConfig.adbPath());
    }

    public Adb(String adbPath) {
        this.adbPath = adbPath;
    }

    /** Serial numbers of devices in the "device" state (connected and authorized). */
    public List<String> connectedDevices() {
        List<String> serials = new ArrayList<>();
        for (String line : text("devices").split("\\R")) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 2 && parts[1].equals("device")) {
                serials.add(parts[0]);
            }
        }
        return serials;
    }

    public boolean isInstalled(String packageName) {
        return text("shell", "pm", "list", "packages", packageName)
                .lines()
                .anyMatch(line -> line.trim().equals("package:" + packageName));
    }

    /**
     * Starts the app's launcher activity, the same as tapping its icon on the home screen.
     * Uses "am start" instead of "monkey": on the Redmi A5 monkey first scans old tombstones,
     * which made every launch take about a minute.
     */
    public void launch(String packageName) {
        launchAndMeasure(packageName);
    }

    /** Launches the app like {@link #launch} and returns the launch time ("TotalTime") in milliseconds. */
    public int launchAndMeasure(String packageName) {
        return AdbOutput.launchTotalTimeMs(text("shell", "am", "start", "-W", "-n", launcherActivity(packageName)));
    }

    /** Raw "dumpsys meminfo" report of the app (memory usage by category, in kilobytes). */
    public String memoryInfo(String packageName) {
        return text("shell", "dumpsys", "meminfo", packageName);
    }

    /** Launcher activity component, e.g. "com.unity.trashdash/com.unity3d.player.UnityPlayerActivity". */
    public String launcherActivity(String packageName) {
        return text("shell", "cmd", "package", "resolve-activity", "--brief",
                "-c", "android.intent.category.LAUNCHER", packageName)
                .lines()
                .map(String::trim)
                .filter(line -> line.startsWith(packageName + "/"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No launcher activity found for " + packageName));
    }

    public void forceStop(String packageName) {
        text("shell", "am", "force-stop", packageName);
    }

    /** Process id of the running app, empty if the app is not running. */
    public Optional<Integer> pidOf(String packageName) {
        String out = text("shell", "pidof", packageName).trim();
        if (out.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(out.split("\\s+")[0]));
    }

    /** The activity currently on top of the screen, e.g. "com.unity.trashdash/com.unity3d.player.UnityPlayerActivity". */
    public String topResumedActivity() {
        return text("shell", "dumpsys", "activity", "activities")
                .lines()
                .filter(line -> line.contains("topResumedActivity") || line.contains("mResumedActivity"))
                .findFirst()
                .orElse("")
                .trim();
    }

    /** System property of the device, e.g. "ro.product.model" or "ro.build.version.release". */
    public String property(String name) {
        return text("shell", "getprop", name).trim();
    }

    /** Installed app version as "versionName (versionCode)", e.g. "1.0 (350)". */
    public String appVersion(String packageName) {
        String dump = text("shell", "dumpsys", "package", packageName);
        String name = valueAfter(dump, "versionName=");
        String code = valueAfter(dump, "versionCode=");
        return name + " (" + code + ")";
    }

    private static String valueAfter(String text, String key) {
        int start = text.indexOf(key);
        if (start < 0) {
            return "unknown";
        }
        start += key.length();
        int end = start;
        while (end < text.length() && !Character.isWhitespace(text.charAt(end))) {
            end++;
        }
        return text.substring(start, end);
    }

    public void clearLogcat() {
        text("logcat", "-c");
    }

    /** Dumps the current logcat buffer in the "brief" format: "E/Tag(pid): message". */
    public String logcat() {
        return text("logcat", "-d", "-v", "brief");
    }

    /** PNG screenshot of the device screen. */
    public byte[] screenshot() {
        return run("exec-out", "screencap", "-p");
    }

    private String text(String... args) {
        return new String(run(args), StandardCharsets.UTF_8);
    }

    private byte[] run(String... args) {
        List<String> command = new ArrayList<>();
        command.add(adbPath);
        command.addAll(Arrays.asList(args));
        try {
            Process process = new ProcessBuilder(command).redirectErrorStream(false).start();
            byte[] out;
            try (InputStream in = process.getInputStream()) {
                out = readAll(in);
            }
            if (!process.waitFor(COMMAND_TIMEOUT_SEC, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                throw new IllegalStateException("adb timed out: " + String.join(" ", command));
            }
            return out;
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot run adb (" + adbPath + "). Is Android platform-tools installed?", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while running adb", e);
        }
    }

    private static byte[] readAll(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        in.transferTo(buffer);
        return buffer.toByteArray();
    }
}
