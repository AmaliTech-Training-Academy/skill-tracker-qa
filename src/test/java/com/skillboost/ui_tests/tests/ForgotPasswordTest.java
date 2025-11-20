package com.skillboost.ui_tests.tests;

import com.skillboost.ui_tests.base.UiBaseTest;
import com.skillboost.ui_tests.pages.ForgotPasswordPage;
import com.skillboost.ui_tests.pages.HomePage;
import com.skillboost.ui_tests.pages.LoginPage;
import com.skillboost.ui_tests.utils.AssertionLogger;
import com.skillboost.ui_tests.utils.JsonDataReader;
import com.skillboost.ui_tests.utils.WaitUtils;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("UI Tests")
@Feature("Forgot Password Page")
@Story("User accesses Forgot Password page and verifies its elements")
public class ForgotPasswordTest extends UiBaseTest {

    private static final String LOGIN_PATH = "/login";
    private static final String FORGOT_PASSWORD_PATH = "/forgot-password";
    private static final String TESTDATA_FILE = "forgot-password-data.json";


    private HomePage homePage;
    private LoginPage loginPage;
    private ForgotPasswordPage forgotPasswordPage;

    @BeforeEach
    void setUpTest() {
        homePage = new HomePage(driver);
    }

    @Test
    @Order(1)
    @DisplayName("Verify Forgot Password page elements")
    @Description("Ensures all key elements on the Forgot Password page are visible and interactable.")
    @Severity(SeverityLevel.CRITICAL)
    public void testForgotPasswordPageElements() {
        log.info("Starting test: Verify Forgot Password page elements.");

        // Step 1: Navigate from Homepage to Login Page
        homePage.clickLoginButton();
        log.info("Clicked Login button.");

        // Step 2: Wait for Login page to load and verify visibility
        WaitUtils.waitForUrlContains(LOGIN_PATH);
        loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());
        log.info("Login page loaded successfully and elements are visible.");

        // Step 3: Click "Forgot Password?" link
        loginPage.clickForgotPassword();
        log.info("Clicked 'Forgot Password?' link.");

        // Step 4: Wait for Forgot Password page to load and verify key elements
        WaitUtils.waitForUrlContains(FORGOT_PASSWORD_PATH);
        forgotPasswordPage = new ForgotPasswordPage(driver);
        WaitUtils.waitForVisibility(forgotPasswordPage.getLogoElement());
        WaitUtils.waitForVisibility(forgotPasswordPage.getForgotPasswordHeaderElement());
        log.info("Navigated to Forgot Password page successfully.");

        // Step 5: Assertions for Forgot Password page elements
        AssertionLogger.assertTrueWithLog(forgotPasswordPage.isLogoVisible(),
                "Logo is visible on Forgot Password page.");
        AssertionLogger.assertTrueWithLog(forgotPasswordPage.isForgotPasswordHeaderVisible(),
                "'Forgot Password?' header is visible.");
        AssertionLogger.assertFalseWithLog(forgotPasswordPage.isSendLinkButtonEnabled(),
                "'Send Link' button is disabled initially.");
        AssertionLogger.assertTrueWithLog(forgotPasswordPage.isTryDifferentEmailButtonVisible(),
                "'Try a Different Email' button is visible.");

        log.info("All key elements on Forgot Password page are visible and interactable.");
    }



    @Test
    @Order(2)
    @DisplayName("Verify 'Send Link' button activates after entering a valid email")
    @Description("Ensures that the 'Send Link' button becomes enabled once a valid email is entered on the Forgot Password page.")
    @Severity(SeverityLevel.CRITICAL)
    public void testSendLinkButtonBecomesActive() {
        log.info("Starting test: Verify 'Send Link' button activation with valid email.");

        // Step 1: Navigate to Login Page
        homePage.clickLoginButton();
        WaitUtils.waitForUrlContains(LOGIN_PATH);
        loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());
        log.info("Login page loaded successfully.");

        // Step 2: Navigate to Forgot Password page
        WaitUtils.waitForVisibility(loginPage.getForgotPasswordLink());
        loginPage.clickForgotPassword();
        log.info("Clicked 'Forgot Password?' link.");

        WaitUtils.waitForUrlContains(FORGOT_PASSWORD_PATH);
        forgotPasswordPage = new ForgotPasswordPage(driver);
        WaitUtils.waitForVisibility(forgotPasswordPage.getForgotPasswordHeaderElement());
        log.info("Forgot Password page loaded successfully.");

        // Step 3: Load valid email from JSON and enter it
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        String validEmail = testData.get("validEmail").get("email");
        forgotPasswordPage.enterEmail(validEmail);
        log.info("Entered valid email: {}", validEmail);

        // Step 4: Wait for 'Send Link' button to become enabled
        WaitUtils.waitForCondition(
                driver,
                d -> forgotPasswordPage.isSendLinkButtonEnabled(),
                8,
                "'Send Link' button did not become enabled within 8 seconds."
        );

        // Step 5: Assert button is enabled
        AssertionLogger.assertTrueWithLog(
                forgotPasswordPage.isSendLinkButtonEnabled(),
                "'Send Link' button is active after entering valid email."
        );

        log.info("Successfully verified 'Send Link' button activation.");
    }



    @Test
    @Order(3)
    @DisplayName("Verify 'Send Link' button remains disabled when invalid email is entered")
    @Description("Ensures that the 'Send Link' button stays inactive when an invalid email is entered on the Forgot Password page.")
    @Severity(SeverityLevel.CRITICAL)
    public void testSendLinkButtonRemainsDisabledForInvalidEmail() {
        log.info("Starting test: Verify 'Send Link' button remains disabled for invalid email.");

        // Step 1: Navigate to Login Page
        homePage.clickLoginButton();
        WaitUtils.waitForUrlContains(LOGIN_PATH);
        loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());
        log.info("Login page loaded successfully.");

        // Step 2: Navigate to Forgot Password page
        WaitUtils.waitForVisibility(loginPage.getForgotPasswordLink());
        loginPage.clickForgotPassword();
        log.info("Clicked 'Forgot Password?' link.");

        WaitUtils.waitForUrlContains(FORGOT_PASSWORD_PATH);
        forgotPasswordPage = new ForgotPasswordPage(driver);
        WaitUtils.waitForVisibility(forgotPasswordPage.getForgotPasswordHeaderElement());
        log.info("Forgot Password page loaded successfully.");

        // Step 3: Load and enter invalid email
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        String invalidEmail = testData.get("invalidEmail").get("email");
        forgotPasswordPage.enterEmail(invalidEmail);
        log.info("Entered invalid email: {}", invalidEmail);

        // Step 4: Wait for Send Link button to remain disabled
        WaitUtils.waitForCondition(
                driver,
                d -> !forgotPasswordPage.isSendLinkButtonEnabled(),
                8,
                "'Send Link' button should remain disabled but became enabled for invalid email."
        );

        // Step 5: Assert button is disabled
        AssertionLogger.assertFalseWithLog(
                forgotPasswordPage.isSendLinkButtonEnabled(),
                "'Send Link' button remains disabled for invalid email input."
        );

        log.info("Verified 'Send Link' button remains disabled for invalid email successfully.");
    }



    @Test
    @Order(4)
    @DisplayName("Verify navigation from Forgot Password page back to Login page")
    @Description("Ensures that clicking the 'Back to Login' button on the Forgot Password page correctly navigates the user to the Login page.")
    @Severity(SeverityLevel.CRITICAL)
    public void testNavigateBackToLoginFromForgotPassword() {
        log.info("Starting test: Verify navigation from Forgot Password page back to Login page.");

        // Step 1: Navigate to Login Page
        homePage.clickLoginButton();
        WaitUtils.waitForUrlContains(LOGIN_PATH);
        loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());
        log.info("Login page loaded successfully.");

        // Step 2: Navigate to Forgot Password page
        WaitUtils.waitForVisibility(loginPage.getForgotPasswordLink());
        loginPage.clickForgotPassword();
        log.info("Clicked 'Forgot Password?' link.");
        WaitUtils.waitForUrlContains(FORGOT_PASSWORD_PATH);
        forgotPasswordPage = new ForgotPasswordPage(driver);
        WaitUtils.waitForVisibility(forgotPasswordPage.getForgotPasswordHeaderElement());
        log.info("Forgot Password page loaded successfully.");

        // Step 3: Click 'Back to Login' button
        WaitUtils.waitForVisibility(forgotPasswordPage.getBackToLoginButtonElement());
        forgotPasswordPage.clickBackToLogin();
        log.info("Clicked 'Back to Login' button.");

        // Step 4: Verify navigation back to Login page
        WaitUtils.waitForUrlContains(LOGIN_PATH);
        WaitUtils.waitForVisibility(loginPage.getLogo());  // ensure page is fully loaded
        AssertionLogger.assertTrueWithLog(
                loginPage.isLogoVisible(),
                "Successfully navigated back to Login page from Forgot Password page."
        );

        log.info("Test completed: Navigation from Forgot Password page to Login page verified successfully.");
    }



}
