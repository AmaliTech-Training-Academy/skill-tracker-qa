package com.skillboost.ui_tests.pages;

import com.skillboost.ui_tests.base.UiBasePage;
import com.skillboost.ui_tests.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object Model for the SkillBoost Homepage.
 * Encapsulates web elements and user interactions for navigation and visibility checks.
 */
public class HomePage extends UiBasePage {

    // --- Constructor ---
    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // --- Navbar Elements ---
    @FindBy(css = "div.navbar-container a.logo")
    private WebElement logo;

    @FindBy(css = "div.nav-menu a.active")
    private WebElement platformLink;

    @FindBy(css = "nav.navbar a:nth-child(2)")
    private WebElement howItWorksLink;

    @FindBy(css = "nav.navbar a:nth-child(3)")
    private WebElement skillsLink;

    @FindBy(css = "nav.navbar a:nth-child(4)")
    private WebElement pricingLink;

    @FindBy(xpath = "//a[@class='login-btn']")
    private WebElement loginButton;

    @FindBy(css = ".signup-btn")
    private WebElement signupButton;

    // --- Body Elements ---
    @FindBy(css = ".btn-primary")
    private WebElement startPracticingButton;

    @FindBy(css = ".btn-outline")
    private WebElement learnMoreButton;

    @FindBy(css = "button[type='button']")
    private WebElement meetLuminaButton;

    @FindBy (xpath = "//h2[normalize-space()='SkillDev Pricing Models']")
    private WebElement seePricingOptions;


    // --- Page Actions ---
    public boolean clickStartPracticing() {
        click(startPracticingButton);
        return true;
    }

    public void clickLearnMore() {
        click(learnMoreButton);
    }

    public void clickMeetLumina() {
        click(meetLuminaButton);
    }
    public WebElement clickLoginButton() {
        try {
            WaitUtils.waitForClickability(loginButton).click();
            log.info("Clicked Login button successfully.");
        } catch (Exception e) {
            log.error("Failed to click Login button: {}", e.getMessage());
            throw e;
        }
        return null;
    }

    public void navigateToSignup() {
        click(signupButton);
    }

    public void openPricingSection() {
        click(pricingLink);
    }

    public void scrollToPricingOptions() {
        scrollToElement(seePricingOptions);
        WaitUtils.waitForVisibility(seePricingOptions);
    }

    public WebElement getLogoElement() {
        return logo;
    }


    // --- Validations ---
    public boolean isLogoDisplayed() {
        return isElementVisible(logo);
    }

    public boolean isStartPracticingButtonVisible() {
        return isElementVisible(startPracticingButton);
    }

    public boolean isPricingOptionsVisible() {
        return isElementVisible(seePricingOptions);
    }
}
