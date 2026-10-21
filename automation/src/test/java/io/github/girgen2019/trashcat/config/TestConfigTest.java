package io.github.girgen2019.trashcat.config;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for TestConfig: defaults come from config.properties, -D system properties win.
 */
@Feature("Configuration")
@DisplayName("TestConfig")
class TestConfigTest {

    @AfterEach
    void clearOverrides() {
        System.clearProperty("android.package");
        System.clearProperty("alttester.port");
        System.clearProperty("android.stabilitySec");
    }

    @Test
    @DisplayName("Reads defaults from config.properties")
    void readsDefaults() {
        assertThat(TestConfig.androidPackage()).isEqualTo("com.unity.trashdash");
        assertThat(TestConfig.port()).isEqualTo(13000);
        assertThat(TestConfig.host()).isEqualTo("127.0.0.1");
        assertThat(TestConfig.stabilitySec()).isEqualTo(30);
    }

    @Test
    @DisplayName("System property (-D) overrides the file value")
    void systemPropertyWins() {
        System.setProperty("android.package", "com.example.othergame");
        System.setProperty("alttester.port", "13001");

        assertThat(TestConfig.androidPackage()).isEqualTo("com.example.othergame");
        assertThat(TestConfig.port()).isEqualTo(13001);
    }

    @Test
    @DisplayName("Trims spaces around values")
    void trimsValues() {
        System.setProperty("android.stabilitySec", "  5  ");

        assertThat(TestConfig.stabilitySec()).isEqualTo(5);
    }

    @Test
    @DisplayName("Fails with a clear error on a non-numeric number")
    void failsOnInvalidNumber() {
        System.setProperty("alttester.port", "abc");

        assertThatThrownBy(TestConfig::port).isInstanceOf(NumberFormatException.class);
    }
}
