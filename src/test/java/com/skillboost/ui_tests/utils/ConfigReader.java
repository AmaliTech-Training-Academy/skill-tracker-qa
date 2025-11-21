package com.skillboost.ui_tests.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * ConfigReader loads and provides environment-specific configuration data
 * for UI automation (Selenium + JUnit 5).
 *
 * Reads from: src/test/resources/ui/config/ui-config.json
 *
 * Improvements:
 * - Safer environment selection
 * - Proper fallback logic
 * - Centralized timeout lookup
 * - CI/CD-safe path resolution
 * - Cleaner and more predictable behavior
 */
public final class ConfigReader {

    private static JsonNode configRoot;
    private static JsonNode activeEnv;

    static {
        loadConfig();
    }

    private ConfigReader() {
    }

    // ─────────────────────────────────────────────
    // ✅ Load and Initialize Config
    // ─────────────────────────────────────────────
    private static void loadConfig() {
        try {
            // CI-friendly path
            String configPath = Paths.get("src", "test", "resources", "ui", "config", "ui-config.json").toString();
            File file = new File(configPath);

            if (!file.exists()) {
                throw new IOException("Config file not found at: " + file.getAbsolutePath());
            }

            ObjectMapper mapper = new ObjectMapper();
            configRoot = mapper.readTree(file);

            // Which environment?
            String env = Optional.ofNullable(System.getProperty("environment"))
                    .orElse(configRoot.path("activeEnvironment").asText("dev"));

            JsonNode envNode = configRoot.path("environments").path(env);

            if (envNode.isMissingNode() || envNode.isEmpty()) {
                throw new RuntimeException("❌ Environment '" + env + "' not found in ui-config.json");
            }

            activeEnv = envNode;

            System.out.println("✅ UI config loaded (environment = " + env + ")");

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to load ui-config.json → " + e.getMessage(), e);
        }
    }

    // ─────────────────────────────────────────────
    // ✅ Base Settings
    // ─────────────────────────────────────────────
    public static String getBaseUrl() {
        return activeEnv.path("baseUrl").asText();
    }

    public static String getBrowser() {
        return activeEnv.path("browser")
                .asText(configRoot.path("default").path("browser").asText("chrome"));
    }

    public static boolean isHeadless() {
        return activeEnv.path("headless")
                .asBoolean(configRoot.path("default").path("headless").asBoolean(false));
    }

    public static boolean isRemote() {
        // Only based on config → DriverFactory has override logic
        return configRoot.path("default").path("remote").asBoolean(false);
    }

    public static String getGridUrl() {
        return configRoot.path("default").path("gridUrl").asText("http://localhost:4444/wd/hub");
    }

    // ─────────────────────────────────────────────
    // ✅ Window Options
    // ─────────────────────────────────────────────
    public static boolean isFullscreen() {
        return configRoot.path("default").path("fullscreen").asBoolean(false);
    }

    public static boolean isMaximize() {
        return configRoot.path("default").path("maximizeWindow").asBoolean(true);
    }

    public static String getBrowserSize() {
        return activeEnv.path("browserSize")
                .asText(configRoot.path("default").path("browserSize").asText("1920x1080"));
    }

    // ─────────────────────────────────────────────
    // ✅ All Timeout Values (Seconds)
    // ─────────────────────────────────────────────
    public static int getImplicitWait() {
        return getTimeout("implicitWait", 10);
    }

    public static int getExplicitWait() {
        return getTimeout("explicitWait", 20);
    }

    public static int getPageLoadTimeout() {
        return getTimeout("pageLoadTimeout", 30);
    }

    private static int getTimeout(String key, int defaultVal) {
        JsonNode timeouts = configRoot.path("default").path("timeouts");
        return timeouts.path(key).asInt(defaultVal);
    }

    // ─────────────────────────────────────────────
    // ✅ Reporting & Allure
    // ─────────────────────────────────────────────
    public static boolean isScreenshotOnFailure() {
        return configRoot.path("default").path("screenshotOnFailure").asBoolean(true);
    }

    public static boolean isSavePageSourceOnFailure() {
        return configRoot.path("default").path("savePageSourceOnFailure").asBoolean(true);
    }

    public static String getAllureResultsDir() {
        return configRoot.path("default").path("allure").path("resultsDirectory")
                .asText("target/allure-results");
    }

    public static String getAllureReportDir() {
        return configRoot.path("default").path("allure").path("reportDirectory")
                .asText("target/allure-report");
    }

    public static boolean isReportingScreenshots() {
        return configRoot.path("default").path("reporting").path("screenshots").asBoolean(true);
    }

    public static boolean isReportingLogs() {
        return configRoot.path("default").path("reporting").path("logs").asBoolean(true);
    }

    public static boolean isVideoRecordingEnabled() {
        return configRoot.path("default").path("reporting").path("videoRecording").asBoolean(false);
    }
}
