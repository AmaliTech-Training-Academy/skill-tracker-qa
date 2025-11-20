package com.skillboost.ui_tests.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * DriverFactory manages WebDriver initialization and cleanup.
 * Supports local and remote (Grid/Docker) execution modes.
 * Reads configuration dynamically from ConfigReader and system properties.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private DriverFactory() {
        // Prevent instantiation
    }

    /**
     * Initializes the WebDriver based on ui-config.json and environment/system variables.
     */
    public static void initializeDriver() {
        if (driverThread.get() != null) {
            return; // Prevent multiple instances per thread
        }

        String browser = System.getProperty("browser", ConfigReader.getBrowser()).toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", String.valueOf(ConfigReader.isHeadless())));
        boolean remote = Boolean.parseBoolean(System.getProperty("remote", String.valueOf(ConfigReader.isRemote())));
        String gridUrl = System.getProperty("gridUrl", ConfigReader.getGridUrl());

        WebDriver driver;

        try {
            // ───────────────────────────────
            // ✅ Remote (Grid/Docker) Mode
            // ───────────────────────────────
            if (remote) {
                switch (browser) {
                    case "chrome" -> {
                        ChromeOptions chromeOptions = new ChromeOptions();
                        if (headless) chromeOptions.addArguments("--headless=new");
                        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                        driver = new RemoteWebDriver(new URL(gridUrl), chromeOptions);
                    }
                    case "firefox" -> {
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        if (headless) firefoxOptions.addArguments("--headless");
                        driver = new RemoteWebDriver(new URL(gridUrl), firefoxOptions);
                    }
                    case "edge" -> {
                        EdgeOptions edgeOptions = new EdgeOptions();
                        if (headless) edgeOptions.addArguments("--headless=new");
                        driver = new RemoteWebDriver(new URL(gridUrl), edgeOptions);
                    }
                    default -> throw new IllegalArgumentException("Unsupported remote browser: " + browser);
                }
            }

            // ───────────────────────────────
            // ✅ Local Mode
            // ───────────────────────────────
            else {
                switch (browser) {
                    case "chrome" -> {
                        ChromeOptions chromeOptions = new ChromeOptions();
                        if (headless) chromeOptions.addArguments("--headless=new");
                        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                        driver = new ChromeDriver(chromeOptions);
                    }
                    case "firefox" -> {
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        if (headless) firefoxOptions.addArguments("--headless");
                        driver = new FirefoxDriver(firefoxOptions);
                    }
                    case "edge" -> {
                        EdgeOptions edgeOptions = new EdgeOptions();
                        if (headless) edgeOptions.addArguments("--headless=new");
                        driver = new EdgeDriver(edgeOptions);
                    }
                    default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
                }
            }

            // ───────────────────────────────
            // ✅ Browser Window Configuration
            // ───────────────────────────────
            if (ConfigReader.isFullscreen()) {
                driver.manage().window().fullscreen();
            } else if (ConfigReader.isMaximize()) {
                driver.manage().window().maximize();
            }

            // Implicit Wait (optional global default)
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));

            driverThread.set(driver);

            System.out.println("✅ WebDriver initialized: " +
                    (remote ? "Remote" : "Local") + " | Browser: " + browser + " | Headless: " + headless);

        } catch (MalformedURLException e) {
            throw new RuntimeException("❌ Invalid Grid URL: " + gridUrl, e);
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to initialize WebDriver for browser: " + browser, e);
        }
    }

    /**
     * Returns the WebDriver instance for the current thread.
     */
    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            initializeDriver();
        }
        return driverThread.get();
    }

    /**
     * Quits and removes the WebDriver instance.
     */
    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
            WaitUtils.resetWait(); // Reset wait after quitting
            System.out.println("🧹 WebDriver session ended successfully.");
        }
    }
}
