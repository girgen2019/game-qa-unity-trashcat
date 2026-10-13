package io.github.girgen2019.trashcat.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Reads config.properties from the classpath. A JVM system property with the same key wins,
 * so CI or a developer can override values without editing the file.
 */
public final class TestConfig {

    private static final Properties PROPS = load();

    private TestConfig() {
    }

    public static String host() {
        return get("alttester.host");
    }

    public static int port() {
        return Integer.parseInt(get("alttester.port"));
    }

    public static String appName() {
        return get("alttester.appName");
    }

    public static int connectTimeoutSec() {
        return Integer.parseInt(get("alttester.connectTimeoutSec"));
    }

    public static boolean enableLogging() {
        return Boolean.parseBoolean(get("alttester.enableLogging"));
    }

    public static double waitTimeoutSec() {
        return Double.parseDouble(get("wait.timeoutSec"));
    }

    private static String get(String key) {
        String value = System.getProperty(key, PROPS.getProperty(key));
        if (value == null) {
            throw new IllegalStateException("Missing config value: " + key);
        }
        return value.trim();
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on the classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return props;
    }
}
