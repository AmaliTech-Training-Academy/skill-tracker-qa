package com.skillboost.ui_tests.base;


import com.skillboost.ui_tests.utils.ConfigReader;
import com.skillboost.ui_tests.utils.DriverFactory;
import com.skillboost.ui_tests.utils.WaitUtils;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base test class for all SkillBoost UI tests.
 * Handles WebDriver setup, teardown, and environment initialization.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class UiBaseTest {

    protected WebDriver driver;
    protected final Logger log = LoggerFactory.getLogger(getClass());



    @BeforeAll
    @Step("Initialize test suite configuration")
    public void beforeAll() {
        log.info("===== Starting SkillBoost UI Test Suite =====");
        log.info("Base URL: {}", ConfigReader.getBaseUrl());
        log.info("Browser: {}", ConfigReader.getBrowser());
        log.info("Headless Mode: {}", ConfigReader.isHeadless());
    }

    @BeforeEach
    @Step("Set up WebDriver before each test")
    public void setUp() {
        log.info("----- Setting up WebDriver instance -----");
        DriverFactory.initializeDriver();
        driver = DriverFactory.getDriver();
        WaitUtils.resetWait();  // ensure fresh waits
        driver.get(ConfigReader.getBaseUrl());
        log.info("Navigated to: {}", ConfigReader.getBaseUrl());
    }

    @AfterEach
    @Step("Clean up after each test")
    public void tearDown() {
        log.info("----- Cleaning up WebDriver instance -----");
        DriverFactory.quitDriver();
    }

    @AfterAll
    @Step("Tear down test suite")
    public void afterAll() {
        log.info("===== SkillBoost UI Test Suite Completed =====");
    }


    // Utility Methods
    @Step("Navigate to path: {path}")
    protected void navigateTo(String path) {
        String fullUrl = ConfigReader.getBaseUrl() + path;
        log.info("Navigating to: {}", fullUrl);
        driver.get(fullUrl);
    }

    @Step("Get current page URL")
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Step("Get current page title")
    protected String getPageTitle() {
        return driver.getTitle();
    }
}

