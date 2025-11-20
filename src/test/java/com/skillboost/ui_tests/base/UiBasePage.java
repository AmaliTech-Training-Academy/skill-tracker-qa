package com.skillboost.ui_tests.base;

import com.skillboost.ui_tests.utils.AssertionLogger;
import com.skillboost.ui_tests.utils.DriverFactory;
import com.skillboost.ui_tests.utils.WaitUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all SkillBoost UI Page Objects.
 * Provides shared Selenium actions, logging, and assertion utilities.
 */
public abstract class UiBasePage {

    protected final WebDriver driver;
    protected static final Logger log = LoggerFactory.getLogger(UiBasePage.class);

    /**
     * Initializes the UiBasePage with the shared WebDriver instance.
     */
    protected UiBasePage(WebDriver driver) {
        this.driver = driver != null ? driver : DriverFactory.getDriver();
    }


    // ─────────────────────────────
    // Navigation Utilities
    // ─────────────────────────────
    public void navigateTo(String url) {
        log.info("Navigating to URL: {}", url);
        driver.get(url);
    }

    public String getCurrentUrl() {
        String currentUrl = driver.getCurrentUrl();
        log.info("Current URL: {}", currentUrl);
        return currentUrl;
    }

    public String getPageTitle() {
        String title = driver.getTitle();
        log.info("Page title: {}", title);
        return title;
    }

    // ─────────────────────────────
    // Element Finders and Actions
    // ─────────────────────────────
    protected WebElement find(By locator) {
        log.debug("Finding element: {}", locator);
        return WaitUtils.waitForVisibility(locator);
    }

    protected void click(By locator) {
        log.info("Clicking element: {}", locator);
        WebElement element = WaitUtils.waitForClickability(locator);
        element.click();
    }

    protected static void click(WebElement element) {
        try {
            WaitUtils.getWait().until(d -> element.isDisplayed() && element.isEnabled());
            element.click();
            log.info("Clicked on element: {}", element);
        } catch (Exception e) {
            log.error("Failed to click on element: {}", element, e);
            throw e;
        }
    }

    protected void type(By locator, String text) {
        log.info("Typing into element: {} | Text='{}'", locator, text);
        WebElement element = find(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void type(WebElement element, String text) {
        log.info("Typing into WebElement: {} | Text='{}'", element, text);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Gets text from a WebElement directly (useful when @FindBy is used).
     */
    protected String getText(WebElement element) {
        try {
            WaitUtils.waitForVisibility(element);
            String text = element.getText();
            log.info("Extracted text from element -> '{}'", text);
            return text;
        } catch (Exception e) {
            log.error("Failed to get text from WebElement: {}", element, e);
            throw e;
        }
    }


    protected void selectByVisibleText(By locator, String visibleText) {
        log.info("Selecting '{}' from dropdown: {}", visibleText, locator);
        WebElement dropdown = find(locator);
        new Select(dropdown).selectByVisibleText(visibleText);
    }

    protected void hoverOver(By locator) {
        log.info("Hovering over element: {}", locator);
        WebElement element = find(locator);
        new Actions(driver).moveToElement(element).perform();
    }

    // ─────────────────────────────
    // Visibility and Assertions
    // ─────────────────────────────
    protected boolean isElementDisplayed(By locator) {
        try {
            boolean visible = find(locator).isDisplayed();
            log.info("Element displayed check for {} -> {}", locator, visible);
            return visible;
        } catch (NoSuchElementException e) {
            log.warn("Element not found: {}", locator);
            return false;
        }
    }

    protected boolean isElementVisible(By locator) {
        try {
            return WaitUtils.waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            log.debug("Element not visible or not present: {} -> {}", locator, e.getMessage());
            return false;
        }
    }

    protected boolean isElementVisible(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            log.debug("WebElement not visible or stale: {} -> {}", element, e.getMessage());
            return false;
        }
    }

    protected void assertTextEquals(WebElement element, String expectedText, String message) {
        String actualText = getText(element);
        AssertionLogger.assertEqualsWithLog(expectedText, actualText, message);
    }

    // ─────────────────────────────
    // JavaScript & Page Utilities
    // ─────────────────────────────
    protected void scrollIntoView(By locator) {
        WebElement element = find(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        log.info("Scrolled to element: {}", locator);
    }

    protected void waitForText(By locator, String text) {
        log.info("Waiting for text '{}' to appear in element: {}", text, locator);
        WaitUtils.waitForTextToBePresent(locator, text);
    }

    protected void refreshPage() {
        log.info("Refreshing page");
        driver.navigate().refresh();
    }

    protected void goBack() {
        log.info("Navigating back");
        driver.navigate().back();
    }

    protected void goForward() {
        log.info("Navigating forward");
        driver.navigate().forward();
    }

    protected void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", element);

        // Small pause so you can visually observe the scroll
        try {
            Thread.sleep(1500); // 1.5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
