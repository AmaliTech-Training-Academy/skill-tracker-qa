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
            return;
        }

        // Read browser
        String browser = System.getProperty("browser", ConfigReader.getBrowser()).toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", String.valueOf(ConfigReader.isHeadless())));

        // Detect remote execution
        String envRemoteUrl = System.getenv("SELENIUM_REMOTE_URL"); // NEW
        String configGridUrl = ConfigReader.getGridUrl();
        String sysGridUrl = System.getProperty("gridUrl");

        // Final Grid URL priority → ENV > System Property > config file
        String finalGridUrl =
                (envRemoteUrl != null && !envRemoteUrl.isEmpty()) ? envRemoteUrl :
                        (sysGridUrl != null && !sysGridUrl.isEmpty()) ? sysGridUrl :
                                configGridUrl;

        // Final remote switch → true if SELENIUM_REMOTE_URL exists, OR system says remote=true
        boolean remote =
                (envRemoteUrl != null && !envRemoteUrl.isEmpty()) ||
                        Boolean.parseBoolean(System.getProperty("remote", String.valueOf(ConfigReader.isRemote())));

        WebDriver driver;

        try {
            if (remote) {
                // Remote WebDriver mode
                System.out.println("🔗 Using RemoteWebDriver @ " + finalGridUrl);

                switch (browser) {
                    case "chrome" -> {
                        ChromeOptions options = new ChromeOptions();
                        if (headless) options.addArguments("--headless=new");
                        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                        driver = new RemoteWebDriver(new URL(finalGridUrl), options);
                    }
                    case "firefox" -> {
                        FirefoxOptions options = new FirefoxOptions();
                        if (headless) options.addArguments("--headless");
                        driver = new RemoteWebDriver(new URL(finalGridUrl), options);
                    }
                    case "edge" -> {
                        EdgeOptions options = new EdgeOptions();
                        if (headless) options.addArguments("--headless=new");
                        driver = new RemoteWebDriver(new URL(finalGridUrl), options);
                    }
                    default -> throw new IllegalArgumentException("Unsupported remote browser: " + browser);
                }

            } else {
                // Local webdriver mode
                switch (browser) {
                    case "chrome" -> {
                        ChromeOptions options = new ChromeOptions();
                        if (headless) options.addArguments("--headless=new");
                        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                        driver = new ChromeDriver(options);
                    }
                    case "firefox" -> {
                        FirefoxOptions options = new FirefoxOptions();
                        if (headless) options.addArguments("--headless");
                        driver = new FirefoxDriver(options);
                    }
                    case "edge" -> {
                        EdgeOptions options = new EdgeOptions();
                        if (headless) options.addArguments("--headless=new");
                        driver = new EdgeDriver(options);
                    }
                    default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
                }
            }

            // Window config
            if (ConfigReader.isFullscreen()) driver.manage().window().fullscreen();
            else if (ConfigReader.isMaximize()) driver.manage().window().maximize();

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));

            driverThread.set(driver);

            System.out.println("✅ WebDriver initialized → Mode: " + (remote ? "REMOTE" : "LOCAL") +
                    ", Browser: " + browser + ", Headless: " + headless);

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to initialize WebDriver in " + (remote ? "REMOTE" : "LOCAL") + " mode", e);
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
