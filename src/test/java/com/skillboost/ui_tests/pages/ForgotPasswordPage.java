package com.skillboost.ui_tests.pages;

import com.skillboost.ui_tests.base.UiBasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object Model for the SkillBoost "Forgot Password" Page.
 * Handles interactions and visibility checks for password recovery.
 */
public class ForgotPasswordPage extends UiBasePage {

    // --- Constructor ---
    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // --- Locators ---
    @FindBy(css = "div[class='navbar-container'] a[class='logo']")
    private WebElement logo;

    @FindBy(css = "div[class='onboarding-login'] h1")
    private WebElement forgotPasswordHeader; // "Forgot Password?"

    @FindBy(xpath = "//input[@id='email']")
    private WebElement emailInput;

    @FindBy(css = "button[type='submit']")
    private WebElement sendLinkButton;

    @FindBy(css = "div[class='resend-and-retry'] button:nth-child(1)")
    private WebElement resendEmailButton;

    @FindBy(xpath = "//button[normalize-space()='Try a different email']")
    private WebElement tryDifferentEmailButton;

    @FindBy(xpath = "//div[@class='alert-content']")
    private WebElement failureNotification;

    @FindBy(xpath = "//a[normalize-space()='Back to Login']") //
    private WebElement backToLoginButton;

    // --- Actions ---
    public void enterEmail(String email) {
        type(emailInput, email);
    }

    public void clickSendLink() {
        click(sendLinkButton);
    }

    public void clickResendEmail() {
        click(resendEmailButton);
    }

    public void clickTryDifferentEmail() {
        click(tryDifferentEmailButton);
    }

    // --- Visibility & Validation Methods (Boolean for assertions) ---
    public boolean isLogoVisible() {
        return isElementVisible(logo);
    }

    public boolean isForgotPasswordHeaderVisible() {
        return isElementVisible(forgotPasswordHeader);
    }

    public boolean isSendLinkButtonEnabled() {
        return sendLinkButton.isEnabled();
    }

    public boolean isResendEmailButtonVisible() {
        return isElementVisible(resendEmailButton);
    }

    public boolean isTryDifferentEmailButtonVisible() {
        return isElementVisible(tryDifferentEmailButton);
    }

    public boolean isFailureNotificationVisible() {
        return isElementVisible(failureNotification);
    }

    public String getFailureNotificationText() {
        return getText(failureNotification);
    }

    // --- Getters for WebElements (for WaitUtils) ---
    public WebElement getLogoElement() {
        return logo;
    }

    public WebElement getForgotPasswordHeaderElement() {
        return forgotPasswordHeader;
    }

    public WebElement getEmailInputElement() {
        return emailInput;
    }

    public WebElement getSendLinkButtonElement() {
        return sendLinkButton;
    }

    public WebElement getResendEmailButtonElement() {
        return resendEmailButton;
    }

    public WebElement getTryDifferentEmailButtonElement() {
        return tryDifferentEmailButton;
    }

    public WebElement getFailureNotificationElement() {
        return failureNotification;
    }

    public void clickBackToLogin() {
        click(backToLoginButton);
    }

    public WebElement getBackToLoginButtonElement() {
        return backToLoginButton;
    }
}
