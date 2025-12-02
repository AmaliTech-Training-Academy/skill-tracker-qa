package com.skillboost.ui_tests.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.DriverManager;
import java.time.Duration;
import java.util.List;

import static ch.qos.logback.core.spi.ComponentTracker.DEFAULT_TIMEOUT;

public final class WaitUtils {

    private static final ThreadLocal<WebDriverWait> waitThread = new ThreadLocal<>();
    private static final ThreadLocal<String> sessionIdThread = new ThreadLocal<>();

    private WaitUtils() {}

    public static WebDriverWait getWait() {
        WebDriver driver = DriverFactory.getDriver();

        if (driver == null) {
            throw new IllegalStateException("WebDriver is null. Ensure DriverFactory initialises the driver.");
        }

        String currentSessionId = ((HasCapabilities) driver).getCapabilities()
                .getCapability("webdriver.remote.sessionid") != null
                ? ((HasCapabilities) driver).getCapabilities()
                .getCapability("webdriver.remote.sessionid")
                .toString()
                : "local-driver"; // For local non-remote WebDriver

        WebDriverWait wait = waitThread.get();
        String storedSessionId = sessionIdThread.get();

        // Create a new wait instance if:
        // 1. missing, or
        // 2. driver restarted (session ID changed)
        if (wait == null || storedSessionId == null || !currentSessionId.equals(storedSessionId)) {
            int timeout = ConfigReader.getPageLoadTimeout();
            wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            waitThread.set(wait);
            sessionIdThread.set(currentSessionId);
        }

        return wait;
    }

    // ------------------------------------------------------------------
    // Wait by WebElement
    // ------------------------------------------------------------------

    public static WebElement waitForVisibility(WebElement element) {
        return getWait().until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForClickability(WebElement element) {
        return getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    // ------------------------------------------------------------------
    // Wait by Locator
    // ------------------------------------------------------------------

    public static WebElement waitForVisibility(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickability(By locator) {
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForPresence(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static List<WebElement> waitForAllVisible(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean waitForInvisibility(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForTextToBePresent(By locator, String text) {
        getWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static WebElement waitForReady(By locator) {
        WebElement element = waitForVisibility(locator);
        getWait().until(ExpectedConditions.elementToBeClickable(locator));
        return element;
    }

    // ------------------------------------------------------------------
    // Custom waits
    // ------------------------------------------------------------------

    public static void waitForUrlContains(String partialUrl) {
        getWait().until(ExpectedConditions.urlContains(partialUrl));
    }

    public static void waitForCondition(WebDriver driver,
                                        java.util.function.Function<WebDriver, Boolean> condition,
                                        int timeoutInSeconds,
                                        String failureMessage) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                    .until(d -> {
                        try { return condition.apply(d); }
                        catch (Exception ignored) { return false; }
                    });
        } catch (TimeoutException e) {
            throw new TimeoutException(failureMessage);
        }
    }

    public static void resetWait() {
        waitThread.remove();
        sessionIdThread.remove();
    }


}
