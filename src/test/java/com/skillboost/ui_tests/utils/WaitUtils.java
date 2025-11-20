package com.skillboost.ui_tests.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Utility class for explicit waits.
 * Provides reusable methods to handle synchronization in UI tests.
 * Ensures stable test execution across dynamic web pages.
 */
public final class WaitUtils {

    private static final ThreadLocal<WebDriverWait> waitThread = new ThreadLocal<>();

    private WaitUtils() {
        // Prevent instantiation
    }

    /**
     * Retrieves the current thread's WebDriverWait instance.
     * Initializes a new one if it doesn't exist.
     */
    public static WebDriverWait getWait() {
        if (waitThread.get() == null) {
            WebDriver driver = DriverFactory.getDriver();
            int timeoutSec = ConfigReader.getPageLoadTimeout(); // should return seconds, not ms
            waitThread.set(new WebDriverWait(driver, Duration.ofSeconds(timeoutSec)));
        }
        return waitThread.get();
    }

    // ─────────────────────────────────────────────
    // ✅ Wait Methods by WebElement
    // ─────────────────────────────────────────────

    public static WebElement waitForVisibility(WebElement element) {
        return getWait().until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForClickability(WebElement element) {
        return getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    // ─────────────────────────────────────────────
    // ✅ Wait Methods by Locator (By)
    // ─────────────────────────────────────────────

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

    public static void waitForTextToBePresent(By locator, String text) {
        getWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean waitForInvisibility(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits until an element is both visible and clickable.
     */
    public static WebElement waitForReady(By locator) {
        WebElement element = waitForVisibility(locator);
        getWait().until(ExpectedConditions.elementToBeClickable(locator));
        return element;
    }

    // ─────────────────────────────────────────────
    // ✅ Custom Utility Waits
    // ─────────────────────────────────────────────

    /**
     * Waits for a specific URL fragment to appear in the current browser URL.
     */
    public static void waitForUrlContains(String partialUrl) {
        getWait().until(ExpectedConditions.urlContains(partialUrl));
    }

    /**
     * Waits for a custom condition (lambda or ExpectedCondition) to be true within a timeout.
     *
     * Example:
     * WaitUtils.waitForCondition(driver, d -> element.isDisplayed(), 5, "Element not visible in time");
     */
    public static void waitForCondition(WebDriver driver,
                                        java.util.function.Function<WebDriver, Boolean> condition,
                                        int timeoutInSeconds,
                                        String failureMessage) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(d -> {
                try {
                    return condition.apply(d);
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (TimeoutException e) {
            throw new TimeoutException(failureMessage);
        }
    }

    /**
     * Resets the ThreadLocal WebDriverWait instance (e.g., after driver restart).
     */
    public static void resetWait() {
        waitThread.remove();
    }
}
