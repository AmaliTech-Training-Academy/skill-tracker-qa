package com.skillboost.ui_tests.pages;

import com.skillboost.ui_tests.base.UiBasePage;
import com.skillboost.ui_tests.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object representing the Login Page of SkillBoost.
 * Encapsulates all UI elements and interactions for user login.
 */
public class LoginPage extends UiBasePage {

    // --- Constructor ---
    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // --- Elements ---

    // Header and Branding
    @FindBy(css = "div.navbar-container a.logo")
    private WebElement logo;

    @FindBy(css = "div.onboarding-login h1")
    private WebElement welcomeText;

    // Login Form Elements
    @FindBy(xpath = "//input[@id='email']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordInput;

    @FindBy(css = "img[alt='hidePassword']")
    private WebElement passwordVisibilityToggle;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(css = "a[routerlink='/forgot-password']")
    private WebElement forgotPasswordLink;

    // Signup & OAuth Options
    @FindBy(css = "p.login-donthave-account a")
    private WebElement signupLink;

    @FindBy(css = "button.google-login")
    private WebElement googleLoginButton;

    @FindBy(css = "button.facebook-login")
    private WebElement githubLoginButton;

    @FindBy(css = "div.form-group p")
    private WebElement invalidEmail;

    // Error message below password input
    @FindBy(css = "div.password-field p")
    private WebElement invalidPassword;


    // --- Actions ---

    /** Enters email in the email field */
    public void enterEmail(String email) {
        type(emailInput, email);
    }

    /** Enters password in the password field */
    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    /** Clicks the show/hide password button */
    public void togglePasswordVisibility() {
        click(passwordVisibilityToggle);
    }

    /** Clicks the Login button */
    public void clickLogin() {
        click(loginButton);
    }

    /** Clicks the "Forgot Password" link */
    public void clickForgotPassword() {
        WaitUtils.waitForVisibility(forgotPasswordLink);
        click(forgotPasswordLink);
        log.info("Clicked 'Forgot Password?' link successfully.");
    }

    /** Returns the Forgot Password link element (for waits) */
    public WebElement getForgotPasswordLink() {
        return forgotPasswordLink;
    }

    /** Clicks the "Sign up" link */
    public void clickSignupLink() {
        click(signupLink);
    }

    /** Clicks the Google login button */
    public void clickGoogleLogin() {
        click(googleLoginButton);
    }

    /** Clicks the GitHub login button */
    public void clickGithubLogin() {
        click(githubLoginButton);
    }


    // --- Verifications ---

    /** Checks if logo is visible */
    public boolean isLogoVisible() {
        return isElementVisible(logo);
    }

    public boolean isEmailErrorVisible() {
        return isElementVisible(invalidEmail);

    }
    public boolean isPassWordErrorVisible() {
        return isElementVisible(invalidPassword);
    }

    /** Returns logo WebElement (for waiting or scrolling) */
    public WebElement getLogo() {
        return logo;
    }

    /** Checks if Welcome back text is visible */
    public boolean isWelcomeBackTextVisible() {
        return isElementVisible(welcomeText);
    }

    /** Returns Welcome Back WebElement (for waiting or scrolling) */
    public WebElement getWelcomeBackText() {
        return welcomeText;
    }

    /** Checks if Login button is enabled */
    public boolean isLoginButtonEnabled() {
        return loginButton.isEnabled();
    }

    public WebElement getInvalidEmailMessage() {
        return invalidEmail;
    }

    public WebElement getInvalidPasswordMessage(){
        return invalidPassword;
    }

    public boolean isGoogleSignInButtonVisible(){
        return googleLoginButton.isEnabled();
    }

    public boolean isGitHubSignInButtonVisible(){
        return githubLoginButton.isEnabled();
    }
}
