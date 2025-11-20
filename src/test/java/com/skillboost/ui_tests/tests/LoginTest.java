
package com.skillboost.ui_tests.tests;


import com.skillboost.ui_tests.base.UiBaseTest;
import com.skillboost.ui_tests.pages.ForgotPasswordPage;
import com.skillboost.ui_tests.pages.HomePage;
import com.skillboost.ui_tests.pages.LoginPage;
import com.skillboost.ui_tests.pages.SignupPage;
import com.skillboost.ui_tests.utils.AssertionLogger;
import com.skillboost.ui_tests.utils.JsonDataReader;
import com.skillboost.ui_tests.utils.WaitUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.util.Map;



/**
 * UI Test Suite for SkillBoost Login Page.
 * Covers navigation from homepage and login functionality with different credentials.
 */

public class LoginTest extends UiBaseTest {

    private static final String TESTDATA_FILE = "login-data.json";

    @Test
    @Story("Navigation to Login Page")
    @DisplayName("Verify that user can navigate to Login Page from Homepage")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures clicking the Login button on the homepage redirects to the Login Page where main elements are visible.")
    public void verifyUserCanNavigateToLoginPage() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        log.info("Navigating to Login Page from Homepage...");
        homePage.clickLoginButton();

        // Wait until URL contains "/login" using the updated WaitUtils
        WaitUtils.waitForUrlContains("/login");
        log.info("URL now contains '/login' – likely on Login Page.");

        // Wait for the main UI elements to be visible
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());

        // Assertions
        AssertionLogger.assertTrueWithLog(loginPage.isLogoVisible(), "Logo is visible on Login Page");
        AssertionLogger.assertTrueWithLog(loginPage.isWelcomeBackTextVisible(), "'Welcome Back' text is visible on Login Page");
    }



    @Test
    @Story("Successful Login Flow")
    @DisplayName("Verify login button is active")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures the login button is active after entering valid credentials from test data file.")
    public void verifyLoginButtonIsActive() {
        HomePage homePage = new HomePage(driver);
        log.info("Navigating to Login Page from Homepage...");
        homePage.clickLoginButton();

        // Wait until the Login page URL is loaded
        WaitUtils.waitForUrlContains("/login");
        log.info("Login page URL loaded.");

        // Initialize LoginPage after navigation
        LoginPage loginPage = new LoginPage(driver);

        // Wait for essential UI elements
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());
        log.info("Login page elements are visible and ready for interaction.");

        // Load valid credentials from JSON
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        String email = testData.get("validUser").get("email");
        String password = testData.get("validUser").get("password");
        log.info("Loaded valid credentials from JSON file.");

        // Enter email and password
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        log.info("Entered valid credentials.");

        // Verify login button is enabled
        AssertionLogger.assertTrueWithLog(
                loginPage.isLoginButtonEnabled(),
                "Login button is enabled after entering valid credentials."
        );
    }



    @Test
    @Story("Login Validation")
    @DisplayName("Verify login button remains disabled when no credentials are entered")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that the login button cannot be clicked when both email and password fields are empty.")
    public void verifyLoginButtonDisabledWhenNoCredentialsEntered() {
        log.info("Navigating to Login Page from Homepage...");
        HomePage homePage = new HomePage(driver);
        homePage.clickLoginButton();

        // Wait for Login page to load
        WaitUtils.waitForUrlContains("/login");
        log.info("Login page URL loaded successfully.");

        // Initialize LoginPage and wait for key elements
        LoginPage loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());
        log.info("Login page UI elements are visible.");

        // Load test data for empty credentials
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        String email = testData.get("emptyUser").get("email");
        String password = testData.get("emptyUser").get("password");
        log.info("Loaded empty credentials from JSON file.");

        // Enter empty credentials
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        log.info("Entered empty credentials.");

        // Verify input fields are empty
        AssertionLogger.assertEqualsWithLog(email, "", "Email input field is empty by default.");
        AssertionLogger.assertEqualsWithLog(password, "", "Password input field is empty by default.");

        // Verify that login button is disabled
        boolean isLoginButtonEnabled = loginPage.isLoginButtonEnabled();
        AssertionLogger.assertFalseWithLog(
                isLoginButtonEnabled,
                "Login button should be disabled when both fields are empty."
        );
        log.info("Verified that login button is correctly disabled with empty credentials.");
    }


    @Test
    @Story("Login Validation")
    @DisplayName("Verify error messages appear for invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures both email and password validation messages appear when invalid credentials are entered and user clicks outside the input fields.")
    public void verifyErrorMessagesForInvalidInputs() {
        log.info("Navigating to Login Page from Homepage...");
        HomePage homePage = new HomePage(driver);
        homePage.clickLoginButton();

        // Wait for Login page to load
        WaitUtils.waitForUrlContains("/login");
        log.info("Login page URL loaded successfully.");

        // Initialize LoginPage and wait for key elements
        LoginPage loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());
        log.info("Login page elements are visible and ready for interaction.");

        // Load invalid credentials from JSON
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        String invalidEmail = testData.get("invalidDetails").get("email");
        String invalidPassword = testData.get("invalidDetails").get("password");
        log.info("Loaded invalid credentials from JSON file.");

        // Enter invalid email and trigger validation
        loginPage.enterEmail(invalidEmail);
        log.info("Entered invalid email.");

        loginPage.enterPassword(invalidPassword); // Focus triggers email validation
        log.info("Entered password to trigger email validation.");

        // Wait for email error message and verify
        WaitUtils.waitForVisibility(loginPage.getInvalidEmailMessage());
        AssertionLogger.assertTrueWithLog(
                loginPage.isEmailErrorVisible(),
                "Email error message is visible for invalid input."
        );
        String emailErrorText = loginPage.getInvalidEmailMessage().getText().trim();
        AssertionLogger.assertEqualsWithLog(
                emailErrorText,
                "Please enter a valid email.",
                "Email error text matches expected."
        );

        // Trigger password validation by clicking elsewhere
        loginPage.getWelcomeBackText().click();
        log.info("Clicked on 'Welcome Back' header to trigger password validation.");

        // Wait for password error message and verify
        WaitUtils.waitForVisibility(loginPage.getInvalidPasswordMessage());
        AssertionLogger.assertTrueWithLog(
                loginPage.isPassWordErrorVisible(),
                "Password error message is visible for invalid input."
        );
        String passwordErrorText = loginPage.getInvalidPasswordMessage().getText().trim();
        AssertionLogger.assertEqualsWithLog(
                passwordErrorText,
                "Password must be at least 8 characters.",
                "Password error text matches expected."
        );

        log.info("Successfully verified inline validation messages for invalid email and password inputs.");
    }


    @Test
    @Story("Forgot Password Navigation")
    @DisplayName("Verify navigation to Forgot Password page when clicked")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that clicking on the 'Forgot Password?' link from the Login page navigates the user to the Forgot Password page.")
    public void verifyForgotPasswordLinkNavigation() {
        log.info("Navigating to Login Page from Homepage...");
        HomePage homePage = new HomePage(driver);
        homePage.clickLoginButton();

        // Wait for Login page to load
        WaitUtils.waitForUrlContains("/login");
        log.info("Login page URL loaded successfully.");

        // Initialize LoginPage and wait for key elements
        LoginPage loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());
        log.info("Login page elements are visible and ready for interaction.");

        // Load test email from JSON
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        String email = testData.get("validDetails").get("email");
        log.info("Loaded valid email from test data.");

        // Enter email and leave password empty
        loginPage.enterEmail(email);
        log.info("Entered email but left password field empty.");

        // Click the "Forgot Password?" link
        loginPage.clickForgotPassword();
        log.info("Clicked on 'Forgot Password?' link.");

        // Initialize ForgotPasswordPage and verify navigation
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
        WaitUtils.waitForUrlContains("/forgot-password");
        log.info("Navigated to Forgot Password page successfully.");

        // Validate Forgot Password page UI elements
        AssertionLogger.assertTrueWithLog(
                forgotPasswordPage.isLogoVisible(),
                "Logo is visible on Forgot Password page."
        );
        AssertionLogger.assertTrueWithLog(
                forgotPasswordPage.isForgotPasswordHeaderVisible(),
                "'Forgot Password?' header is visible."
        );
        AssertionLogger.assertFalseWithLog(
                forgotPasswordPage.isSendLinkButtonEnabled(),
                "'Send Link' button is initially disabled."
        );

        log.info("Successfully verified Forgot Password page UI elements and navigation.");
    }



    @Test
    @Story("Google Sign-In")
    @DisplayName("Verify Sign in with Google button behavior")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that the 'Sign in with Google' button is visible, clickable, and handles integration failure gracefully.")
    public void verifySignInWithGoogleFunctionality() {
        log.info("Navigating to Login Page from Homepage...");
        HomePage homePage = new HomePage(driver);
        homePage.clickLoginButton();

        // Wait for Login page to load
        WaitUtils.waitForUrlContains("/login");
        log.info("Login page URL loaded successfully.");

        // Initialize LoginPage and wait for key elements
        LoginPage loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());
        log.info("Login page elements are visible and ready for interaction.");

        // Verify Google Sign-In button visibility and clickability
        AssertionLogger.assertTrueWithLog(
                loginPage.isGoogleSignInButtonVisible(),
                "Google Sign-In button is visible on the Login page."
        );

        // Click the Google Sign-In button and verify redirection
        log.info("Clicking 'Sign in with Google' button...");
        loginPage.clickGoogleLogin();

        WaitUtils.waitForUrlContains("accounts.google.com");
        String currentUrl = driver.getCurrentUrl();

        AssertionLogger.assertTrueWithLog(
                currentUrl.contains("accounts.google.com"),
                "User was successfully redirected to Google login page: " + currentUrl
        );

        log.info("Google Sign-In redirect verification passed successfully.");
    }



    @Test
    @Story("Third-Party Authentication")
    @DisplayName("Verify GitHub Sign-In Redirect")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that clicking the 'Continue with GitHub' button redirects to GitHub’s login page. Marks as expected behavior if integration is not completed yet.")
    public void verifyGithubSignInRedirect() {
        log.info("Navigating to Login Page from Homepage...");
        HomePage homePage = new HomePage(driver);
        homePage.clickLoginButton();

        // Wait for Login page URL to load
        WaitUtils.waitForUrlContains("/login");
        log.info("Login page URL loaded successfully.");

        // Initialize LoginPage and wait for key elements
        LoginPage loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());
        log.info("Login page elements are visible and ready for interaction.");

        // Click 'Continue with GitHub' button
        log.info("Clicking 'Continue with GitHub' button...");
        loginPage.clickGithubLogin();

        // Wait for redirection to GitHub login page
        WaitUtils.waitForUrlContains("github.com/login");
        String currentUrl = driver.getCurrentUrl();

        AssertionLogger.assertTrueWithLog(
                currentUrl.contains("github.com/login"),
                "User was successfully redirected to GitHub login page: " + currentUrl
        );

        log.info("GitHub Sign-In redirect verification passed successfully.");
    }



    @Test
    @Story("User Registration Navigation")
    @DisplayName("Verify navigation to Signup page from Login page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that clicking on the 'Sign up' link from the Login page redirects the user to the Signup page.")
    public void verifySignupLinkNavigation() {
        log.info("Navigating to Login Page from Homepage...");
        HomePage homePage = new HomePage(driver);
        homePage.clickLoginButton();

        // Wait for Login page to load
        WaitUtils.waitForUrlContains("/login");
        log.info("Login page URL loaded successfully.");

        // Initialize LoginPage and wait for key elements
        LoginPage loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());
        log.info("Login page elements are visible and ready for interaction.");

        // Click 'Sign up' link
        log.info("Clicking on 'Sign up' link...");
        loginPage.clickSignupLink();

        // Wait for navigation to Signup page
        WaitUtils.waitForUrlContains("/signup");
        log.info("Navigated to Signup page successfully.");

        // Initialize SignupPage and wait for header visibility
        SignupPage signupPage = new SignupPage(driver);
        WaitUtils.waitForVisibility(signupPage.createAccountHeader);
        log.info("Signup page header is visible.");

        // Verify key UI elements on Signup page
        AssertionLogger.assertTrueWithLog(signupPage.isLogoVisible(),
                "Logo is visible on Signup page.");
        AssertionLogger.assertTrueWithLog(signupPage.isCreateAccountHeaderVisible(),
                "'Create your free account' header is visible on Signup page.");
        AssertionLogger.assertFalseWithLog(signupPage.isCreateAccountButtonEnabled(),
                "'Create Account' button should initially be disabled until form is valid.");

        log.info("Successfully verified Signup page UI elements and navigation.");
    }


}
