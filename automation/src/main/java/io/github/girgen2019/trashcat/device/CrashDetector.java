package io.github.girgen2019.trashcat.device;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Finds real crash markers of one app in a logcat dump.
 *
 * Plain "any error that mentions the package" does not work: on this Xiaomi device system services
 * (CompositionEngine, UnisocWatchdog) log E-level noise with the package name while the game runs fine.
 * So we only look for signatures that mean the app actually crashed or froze.
 */
public final class CrashDetector {

    private CrashDetector() {
    }

    public static List<String> findCrashLines(String logcat, String packageName) {
        List<Pattern> markers = List.of(
                Pattern.compile("FATAL EXCEPTION"),                                  // Java crash
                Pattern.compile("Fatal signal \\d+"),                                // native (C++/IL2CPP) crash
                Pattern.compile("ANR in " + Pattern.quote(packageName)),             // app not responding
                Pattern.compile("^[EF]/Unity\\s*\\(.*\\b\\w*Exception\\b"),         // unhandled Unity/C# exception
                Pattern.compile("Process " + Pattern.quote(packageName) + " .*has died"));

        return logcat.lines()
                .filter(line -> markers.stream().anyMatch(p -> p.matcher(line).find()))
                .collect(Collectors.toList());
    }
}
