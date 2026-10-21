package io.github.girgen2019.trashcat.report;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * Writes environment.properties into the Allure results folder.
 * Allure shows it as the "Environment" widget on the report overview page.
 */
public final class AllureEnvironment {

    private AllureEnvironment() {
    }

    public static void write(Map<String, String> values) {
        // Same property the Surefire config and allure-junit5 use; IntelliJ runs fall back to the default folder.
        Path dir = Paths.get(System.getProperty("allure.results.directory", "allure-results"));
        Properties props = new Properties();
        props.putAll(values);
        try {
            Files.createDirectories(dir);
            try (Writer out = Files.newBufferedWriter(dir.resolve("environment.properties"), StandardCharsets.UTF_8)) {
                props.store(out, "Test environment");
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write Allure environment to " + dir, e);
        }
    }
}
