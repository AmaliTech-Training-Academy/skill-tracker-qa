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
import java.util.Map;





@Epic("UI Tests")
@Feature("Signup Page")
@Story("User navigates and signs up successfully")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

        WaitUtils.waitForClickability(signupPage.termsCheckbox);
        signupPage.toggleTermsCheckbox();
        log.info("Filled in all fields and accepted terms and conditions.");

        // Step 4: Validate Create Account Button
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountButtonEnabled(),
                "Create Account button is enabled after entering valid inputs."
        );

        WaitUtils.waitForClickability(signupPage.createAccountButton);
        signupPage.clickCreateAccount();
        log.info("Clicked on Create Account button.");

        // Verify Expected Outcome
        WaitUtils.getWait(); // ensures sync time for navigation or response

        String currentUrl = driver.getCurrentUrl();
        AssertionLogger.assertTrueWithLog(
                currentUrl.contains("email-verification"),
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

        homePage.navigateToSignup();

        WaitUtils.waitForVisibility(signupPage.createAccountHeader);
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "Signup page loaded successfully."
        );

        WaitUtils.waitForVisibility(signupPage.emailInput);
        signupPage.clearEmailField();
        signupPage.clearPasswordField();
        signupPage.clearConfirmPasswordField();

        log.info("Ensured that all input fields (email, password, confirm password) are empty.");


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

        homePage.navigateToSignup();

        WaitUtils.waitForVisibility(signupPage.createAccountHeader);
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "Signup page loaded successfully."
        );

        log.info("Loading mismatching password test data from JSON file: {}", TESTDATA_FILE);
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        Map<String, String> validUser = testData.get("mismatchedUser");

        String email = validUser.get("email");
        String password = validUser.get("password");
        String confirmPassword = validUser.get("confirmPassword");

        WaitUtils.waitForVisibility(signupPage.emailInput);
        signupPage.enterEmail(email);
        signupPage.enterPassword(password);
        signupPage.enterConfirmPassword(confirmPassword);

        WaitUtils.waitForClickability(signupPage.termsCheckbox);
        signupPage.toggleTermsCheckbox();

        log.info("Entered valid email and mismatched passwords; terms checkbox selected.");

        // Verify Create Account button is disabled
        boolean isButtonEnabled = signupPage.isCreateAccountButtonEnabled();
        AssertionLogger.assertFalseWithLog(
                isButtonEnabled,
                "Create Account button should be disabled when passwords do not match."
        );

        log.info("Verified that Create Account button is disabled for mismatched passwords.");
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

        WaitUtils.waitForClickability(signupPage.termsCheckbox);
        signupPage.toggleTermsCheckbox();

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

        WaitUtils.waitForClickability(signupPage.termsCheckbox);
        signupPage.toggleTermsCheckbox();

        log.info("Entered valid email and a password not meeting required length. Terms checkbox selected.");

        // Verify Create Account button is disabled
        boolean isButtonEnabled = signupPage.isCreateAccountButtonEnabled();
        AssertionLogger.assertFalseWithLog(
                isButtonEnabled,
                "Create Account button should be disabled when email format is wrong."
        );

        log.info("Verified that Create Account button is disabled for wrong password length.");
    }

    @Test
    @Order(7)
    @Description("Validates that the Create Account button remains disabled when the Terms and Conditions checkbox is not checked.")
    @DisplayName("Verify Create Account button is disabled when terms not accepted")
    @Severity(SeverityLevel.NORMAL)
    public void testSignupButtonDisabledWithoutAcceptingTerms() {
        log.info("Starting test: Create Account button should remain disabled if Terms and Conditions are not accepted.");

        // Step 1: Navigate to Signup Page
        homePage.navigateToSignup();
        WaitUtils.waitForVisibility(signupPage.createAccountHeader);
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "Signup page loaded successfully."
        );

        log.info("Loading valid signup test data from JSON file: {}", TESTDATA_FILE);
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        Map<String, String> validUser = testData.get("validUser");

        String email = validUser.get("email");
        String password = validUser.get("password");
        String confirmPassword = validUser.get("confirmPassword");

        // Step 2: Fill in the form (without checking terms)
        WaitUtils.waitForVisibility(signupPage.emailInput);
        signupPage.enterEmail(email);
        signupPage.enterPassword(password);
        signupPage.enterConfirmPassword(confirmPassword);

        log.info("Filled in email and password fields; Terms checkbox NOT selected.");

        // Step 3: Verify Create Account button is disabled
        boolean isButtonEnabled = signupPage.isCreateAccountButtonEnabled();
        AssertionLogger.assertFalseWithLog(
                isButtonEnabled,
                "Create Account button should be disabled when Terms and Conditions are not accepted."
        );

        log.info("Verified that Create Account button is disabled when Terms and Conditions are unchecked.");
    }


    @Test
    @Order(8)
    @DisplayName("Verify Sign in with Google button behavior")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that the 'Sign in with Google' button is visible, clickable, and correctly redirects to Google login.")
    public void verifySignInWithGoogleFunctionality() {
        log.info("Starting test: Sign in with Google button behavior.");

        // Step 1: Navigate to Signup Page
        homePage.navigateToSignup();
        WaitUtils.waitForVisibility(signupPage.createAccountHeader);
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "Signup page loaded successfully."
        );

        // Step 2: Verify Google Sign-In button visibility and clickability
        WaitUtils.waitForVisibility(SignupPage.googleLoginButton);
        WaitUtils.waitForClickability(SignupPage.googleLoginButton);

        AssertionLogger.assertTrueWithLog(
                SignupPage.googleLoginButton.isDisplayed(),
                "Google Sign-In button is visible on the Signup page."
        );
        AssertionLogger.assertTrueWithLog(
                SignupPage.googleLoginButton.isEnabled(),
                "Google Sign-In button is enabled for user interaction."
        );

        // Step 3: Click Google Sign-In button
        log.info("Clicking 'Sign in with Google' button...");
        SignupPage.clickGoogleLogin();

        // Step 4: Wait for redirect to Google login page
        WaitUtils.waitForUrlContains("accounts.google.com");

        // Step 5: Verify current URL
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertNotNull(currentUrl, "Current URL should not be null after clicking Google Sign-In.");

        AssertionLogger.assertTrueWithLog(
                currentUrl.contains("accounts.google.com"),
                "User successfully redirected to Google login page: " + currentUrl
        );

        log.info("Google Sign-In redirect verification passed successfully.");
    }




    @Test
    @Order(9)
    @DisplayName("Verify Sign in with GitHub button behavior")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that the 'Sign in with GitHub' button is visible, clickable, and correctly redirects to GitHub login.")
    public void verifySignInWithGithubFunctionality() {
        log.info("Starting test: Sign in with GitHub button behavior.");

        // Step 1: Navigate to Signup Page
        homePage.navigateToSignup();
        WaitUtils.waitForVisibility(signupPage.createAccountHeader);
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "Signup page loaded successfully."
        );

        // Step 2: Verify GitHub Sign-In button visibility and clickability
        WaitUtils.waitForVisibility(signupPage.githubLoginButton);
        WaitUtils.waitForClickability(signupPage.githubLoginButton);

        AssertionLogger.assertTrueWithLog(
                signupPage.githubLoginButton.isDisplayed(),
                "GitHub Sign-In button is visible on the Signup page."
        );
        AssertionLogger.assertTrueWithLog(
                signupPage.githubLoginButton.isEnabled(),
                "GitHub Sign-In button is enabled for user interaction."
        );

        // Step 3: Click GitHub Sign-In button
        log.info("Clicking 'Sign in with GitHub' button...");
        signupPage.clickGithubLogin();

        // Step 4: Wait for redirect to GitHub login page
        WaitUtils.waitForUrlContains("github.com/login");

        // Step 5: Verify current URL
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertNotNull(currentUrl, "Current URL should not be null after clicking GitHub Sign-In.");

        AssertionLogger.assertTrueWithLog(
                currentUrl.contains("github.com/login"),
                "User successfully redirected to GitHub login page: " + currentUrl
        );

        log.info("GitHub Sign-In redirect verification passed successfully.");
    }


    @Test
    @Order(10)
    @DisplayName("Verify navigation from Signup to Login page")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that clicking the 'Login' link on the Signup page redirects the user to the Login page successfully.")
    public void testNavigateFromSignupToLogin() {
        log.info("Starting test: Navigate from Signup to Login page.");

        // Navigate to Signup Page
        homePage.navigateToSignup();
        WaitUtils.waitForVisibility(signupPage.createAccountHeader);
        AssertionLogger.assertTrueWithLog(
                signupPage.isCreateAccountHeaderVisible(),
                "Signup page loaded successfully."
        );

        // Click Login link
        WaitUtils.waitForClickability(signupPage.loginLink);
        signupPage.clickLoginLink();
        log.info("Clicked Login link on Signup page.");

        // Wait for Login page to load
        LoginPage loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());

        // Verify Login page elements
        AssertionLogger.assertTrueWithLog(
                loginPage.isWelcomeBackTextVisible(),
                "Login page loaded successfully with 'Welcome Back' text visible."
        );
        AssertionLogger.assertFalseWithLog(
                loginPage.isLoginButtonEnabled(),
                "Login button is not enabled on Login page."
        );

        log.info("Navigation from Signup to Login page verified successfully.");
    }

}
