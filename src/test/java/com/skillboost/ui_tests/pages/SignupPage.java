package com.skillboost.ui_tests.pages;



import com.skillboost.ui_tests.base.UiBasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.skillboost.ui_tests.utils.WaitUtils;

/**
 * Page Object Model for the SkillBoost Signup Page.
 * Handles interactions for creating a new user account.
 */
public class SignupPage extends UiBasePage {

    // --- Constructor ---
    public SignupPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // --- Locators ---

    @FindBy(css = "div[class='navbar-container'] a[class='logo']")
    private WebElement logo;

    @FindBy(css = ".card-title")
    public WebElement createAccountHeader; // e.g. "Create your free account"

    @FindBy(xpath = "//input[@id='email']")
    public WebElement emailInput;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@id='confirmPassword']")
    private WebElement confirmPasswordInput;

    @FindBy(xpath = "//app-input-field[@id='password']//img[@alt='hidePassword']")
    private WebElement showPasswordButton;

    @FindBy(xpath = "//input[@id='terms']")
    public WebElement termsCheckbox;

    @FindBy(css = "button[type='submit']")
    public WebElement createAccountButton; // only active when form valid

    @FindBy(xpath = "//button[normalize-space()='Login']")
    public WebElement loginLink;

    @FindBy(xpath = "//button[@class='btn-social google']")
    public static WebElement googleLoginButton;

    @FindBy(xpath = "//button[@class='btn-social github']")
    public WebElement githubLoginButton;


    // --- Actions ---

    /** Enters email address into the signup email input field */
    public void enterEmail(String email) {
        type(emailInput, email);
    }

    /** Enters password into the signup password input field */
    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    /** Enters confirm password into its input field */
    public void enterConfirmPassword(String confirmPassword) {
        type(confirmPasswordInput, confirmPassword);
    }

    /** Clicks the Terms and Conditions checkbox */
    public void toggleTermsCheckbox() {
        click(termsCheckbox);
    }

    /** Clicks the Create Account button */
    public void clickCreateAccount() {
        click(createAccountButton);
    }

    /** Clicks the Login link to navigate to Login page */
    public void clickLoginLink() {
        click(loginLink);
    }

    /** Clicks the "Sign in with Google" button */
    public static void clickGoogleLogin() {
        click(googleLoginButton);
    }

    /** Clicks the "Sign in with GitHub" button */
    public void clickGithubLogin() {
        click(githubLoginButton);
    }

    // --- Validation / Visibility Checks ---

    /** Checks if the Create Account header text is visible */
    public boolean isCreateAccountHeaderVisible() {
        return isElementVisible(createAccountHeader);
    }

    public static boolean isGoogleSignInVisible(){
        return googleLoginButton.isEnabled();
    }

    public boolean isGitHubSignInVisible(){
        return githubLoginButton.isEnabled();
    }

    /** Checks if the Create Account button is enabled */
    public boolean isCreateAccountButtonEnabled() {
        return createAccountButton.isEnabled();
    }

    /** Checks if the Terms and Conditions checkbox is selected */
    public boolean isTermsCheckboxSelected() {
        return termsCheckbox.isSelected();
    }

    /** Checks if logo is visible */
    public boolean isLogoVisible() {
        return isElementVisible(logo);
    }

    public void clearEmailField() {
        emailInput.clear();
    }

    public void clearPasswordField() {
        emailInput.clear();
    }

    public void clearConfirmPasswordField() {
        confirmPasswordInput.clear();
    }

    public void scrollToCreateAccountButton() {
        scrollToElement(createAccountButton); // uses UiBasePage method
    }



}
