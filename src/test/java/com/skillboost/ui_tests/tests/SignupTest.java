package com.skillboost.ui_tests.tests;


import com.skillboost.ui_tests.base.UiBaseTest;
import com.skillboost.ui_tests.pages.HomePage;
import com.skillboost.ui_tests.pages.LoginPage;
import com.skillboost.ui_tests.pages.SignupPage;
import com.skillboost.ui_tests.utils.AssertionLogger;
import com.skillboost.ui_tests.utils.WaitUtils;
import com.skillboost.ui_tests.utils.JsonDataReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;





@Epic("UI Tests")
@Feature("Signup Page")
@Story("User navigates and signs up successfully")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("regression")
public class SignupTest extends UiBaseTest {

    private HomePage homePage;
    private SignupPage signupPage;

    private static final String TESTDATA_FILE = "signup-data.json";

    @BeforeEach
    void setUpTest() {
        homePage = new HomePage(driver);
        signupPage = new SignupPage(driver);
    }

    @Test
    @Order(1)
    @DisplayName("Verify user navigates from the homepage to signup page")
    @Description("This test ensures that user can navigate from the homepage to the signup page successfully.")
    @Severity(SeverityLevel.CRITICAL)
    public void testNavigateToSignupPage() {
        log.info("Starting test: Navigate from Homepage to Signup Page.");

        // Step 1: Click the Signup button on Homepage
        homePage.navigateToSignup();
        log.info("Clicked 'Signup' button on Homepage.");

        // Step 2: Wait for the Signup page header to be visible
        WaitUtils.waitForVisibility(signupPage.createAccountHeader);

        // Step 3: Validate navigation was successful
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "User successfully navigated to Signup Page and header is visible."
        );

        log.info("Navigation to Signup Page verified successfully.");
    }



    @Test
    @Order(2)
    @Description("Ensures that a user can fill and submit the signup form with valid credentials.")
    @DisplayName("Verify user submits signup form with valid details")
    @Severity(SeverityLevel.CRITICAL)
    public void testSignupWithValidData() {
        log.info("Starting test: signup form submission with valid user data.");

        // Step 1: Navigate to Signup Page
        log.info("Navigating to signup page...");
        homePage.navigateToSignup();
        WaitUtils.waitForVisibility(signupPage.createAccountHeader);

        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "Signup page header is visible — page loaded successfully."
        );

        // Step 2: Load test data from JSON
        log.info("Loading test data from JSON file: {}", TESTDATA_FILE);
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        Map<String, String> validUser = testData.get("validUser");

        String email = validUser.get("email");
        String password = validUser.get("password");
        String confirmPassword = validUser.get("confirmPassword");

        AssertionLogger.assertNotNullWithLog(email, "Email loaded from JSON is not null.");
        AssertionLogger.assertNotNullWithLog(password, "Password loaded from JSON is not null.");
        AssertionLogger.assertNotNullWithLog(confirmPassword, "Confirm Password loaded from JSON is not null.");

        // Step 3: Fill in the Signup Form
        log.info("Filling in signup form fields...");
        WaitUtils.waitForVisibility(signupPage.emailInput);
        signupPage.enterEmail(email);
        signupPage.enterPassword(password);
        signupPage.enterConfirmPassword(confirmPassword);

        // Step 4: Scroll to Create Account button and click it
        log.info("Scrolling to and clicking Create Account button...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signupPage.createAccountButton);
        WaitUtils.waitForClickability(signupPage.createAccountButton);
        signupPage.clickCreateAccount();

        // Step 5: Explicitly wait for redirection to email verification
        log.info("Waiting for redirection to email verification page...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean redirected = wait.until(ExpectedConditions.urlContains("email-verification"));

        AssertionLogger.assertTrueWithLog(
                redirected,
                "User is redirected to a verification or authentication step after signup."
        );

        log.info("Signup form submission test passed successfully.");
    }


    @Test
    @Order(3)
    @Description("Validates that the Create Account button remains disabled when all signup fields are empty.")
    @DisplayName("Verify Create Account button is disabled for empty signup form")
    @Severity(SeverityLevel.NORMAL)
    public void testSignupButtonDisabledForEmptyFields() {
        log.info("Starting test: Create Account button should remain disabled for empty signup form.");

        // Step 1: Navigate to Signup Page
        log.info("Navigating to signup page...");
        homePage.navigateToSignup();

        WaitUtils.waitForVisibility(signupPage.createAccountHeader);
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "Signup page loaded successfully."
        );

        // Step 2: Ensure all fields are empty
        WaitUtils.waitForVisibility(signupPage.emailInput);
        signupPage.clearEmailField();
        signupPage.clearPasswordField();
        signupPage.clearConfirmPasswordField();
        log.info("All input fields (email, password, confirm password) are cleared.");

        // Step 3: Verify that Create Account button is disabled
        WaitUtils.waitForVisibility(signupPage.createAccountButton); // optional but safer
        boolean isButtonEnabled = signupPage.isCreateAccountButtonEnabled();
        AssertionLogger.assertFalseWithLog(
                isButtonEnabled,
                "Create Account button should be disabled when required fields are empty."
        );

        log.info("Verified that Create Account button is disabled for empty signup form.");
    }


    @Test
    @Order(4)
    @Description("Validates that the Create Account button remains disabled when password and confirm password do not match.")
    @DisplayName("Verify Create Account button is disabled for mismatched passwords")
    @Severity(SeverityLevel.NORMAL)
    public void testSignupButtonDisabledForMismatchedPasswords() {
        log.info("Starting test: Create Account button should remain disabled for mismatched passwords.");

        // Step 1: Navigate to Signup Page
        log.info("Navigating to signup page...");
        homePage.navigateToSignup();

        WaitUtils.waitForVisibility(signupPage.createAccountHeader);
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "Signup page loaded successfully."
        );

        // Step 2: Load mismatching password test data
        log.info("Loading mismatching password test data from JSON file: {}", TESTDATA_FILE);
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        Map<String, String> mismatchedUser = testData.get("mismatchedUser");

        String email = mismatchedUser.get("email");
        String password = mismatchedUser.get("password");
        String confirmPassword = mismatchedUser.get("confirmPassword");

        // Safety checks
        AssertionLogger.assertNotNullWithLog(email, "Email loaded from JSON is not null.");
        AssertionLogger.assertNotNullWithLog(password, "Password loaded from JSON is not null.");
        AssertionLogger.assertNotNullWithLog(confirmPassword, "Confirm Password loaded from JSON is not null.");

        // Step 3: Fill the signup form
        log.info("Entering valid email with mismatched passwords...");
        WaitUtils.waitForVisibility(signupPage.emailInput);
        signupPage.enterEmail(email);
        signupPage.enterPassword(password);
        signupPage.enterConfirmPassword(confirmPassword);

        // Step 4: Verify Create Account button is disabled
        WaitUtils.waitForVisibility(signupPage.createAccountButton);
        boolean isButtonEnabled = signupPage.isCreateAccountButtonEnabled();

        AssertionLogger.assertFalseWithLog(
                isButtonEnabled,
                "Create Account button should be disabled when passwords do not match."
        );

        log.info("Verified Create Account button remains disabled for mismatched passwords.");
    }




    @Test
    @Order(5)
    @Description("Validates that the Create Account button remains disabled when email does not meet the required format.")
    @DisplayName("Verify Create Account button is disabled for wrong email format")
    @Severity(SeverityLevel.NORMAL)
    public void testSignupButtonDisabledForWrongEmailFormat() {
        log.info("Starting test: Create Account button should remain disabled for wrong email format.");

        homePage.navigateToSignup();

        WaitUtils.waitForVisibility(signupPage.createAccountHeader);
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "Signup page loaded successfully."
        );

        log.info("Loading wrong email format from data from JSON file: {}", TESTDATA_FILE);
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        Map<String, String> validUser = testData.get("wrongEmailFormat");

        String email = validUser.get("email");
        String password = validUser.get("password");
        String confirmPassword = validUser.get("confirmPassword");

        WaitUtils.waitForVisibility(signupPage.emailInput);
        signupPage.enterEmail(email);
        signupPage.enterPassword(password);
        signupPage.enterConfirmPassword(confirmPassword);


        log.info("Entered invalid email and matching passwords; terms checkbox selected.");

        // ─────────────────────────────────────────────
        // Step 3: Verify Create Account button is disabled
        // ─────────────────────────────────────────────
        boolean isButtonEnabled = signupPage.isCreateAccountButtonEnabled();
        AssertionLogger.assertFalseWithLog(
                isButtonEnabled,
                "Create Account button should be disabled when email format is wrong."
        );

        log.info("Verified that Create Account button is disabled for wrong email format");
    }




    @Test
    @Order(6)
    @Description("Validates that the Create Account button remains disabled when password length not meet the required format.")
    @DisplayName("Verify Create Account button is disabled for wrong password length")
    @Severity(SeverityLevel.NORMAL)
    public void testSignupButtonDisabledForWrongPasswordLength() {
        log.info("Starting test: Create Account button should remain disabled for wrong password length.");

        homePage.navigateToSignup();

        WaitUtils.waitForVisibility(signupPage.createAccountHeader);
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "Signup page loaded successfully."
        );

        log.info("Loading wrong password length from data from JSON file: {}", TESTDATA_FILE);
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        Map<String, String> validUser = testData.get("wrongPasswordLength");

        String email = validUser.get("email");
        String password = validUser.get("password");
        String confirmPassword = validUser.get("confirmPassword");

        WaitUtils.waitForVisibility(signupPage.emailInput);
        signupPage.enterEmail(email);
        signupPage.enterPassword(password);
        signupPage.enterConfirmPassword(confirmPassword);

        log.info("Entered valid email and a password not meeting required length. Terms checkbox selected.");

        // Verify Create Account button is disabled
        boolean isButtonEnabled = signupPage.isCreateAccountButtonEnabled();
        AssertionLogger.assertFalseWithLog(
                isButtonEnabled,
                "Create Account button should be disabled when email format is wrong."
        );

        log.info("Verified that Create Account button is disabled for wrong password length.");
    }


}
