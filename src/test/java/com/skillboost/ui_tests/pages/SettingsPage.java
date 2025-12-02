package com.skillboost.ui_tests.pages;


import com.skillboost.ui_tests.base.UiBasePage;
import com.skillboost.ui_tests.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object Model for the Settings Page.
 * Encapsulates web elements and interactions for updating account information.
 */
public class SettingsPage extends UiBasePage {

    // --- Constructor ---
    public SettingsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // --- Page Elements ---
    @FindBy(xpath = "//h2[normalize-space()='Account Information']")
    private WebElement accountInfoHeader;

    @FindBy(xpath = "//input[@id='fullName']")
    private WebElement fullNameInput;

    @FindBy(xpath = "//input[@id='email']")
    private WebElement emailInput;

    @FindBy(xpath = "//textarea[@id='bio']")
    private WebElement bioInput;

    @FindBy(xpath = "//button[normalize-space()='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//div[@class='alert-content']")
    private WebElement successNotification;

    // --- Page Actions ---
    public void enterFullName(String fullName) {
        WaitUtils.waitForVisibility(fullNameInput);
        fullNameInput.clear();
        fullNameInput.sendKeys(fullName);
    }

    public void enterBio(String bio) {
        WaitUtils.waitForVisibility(bioInput);
        bioInput.clear();
        bioInput.sendKeys(bio);
    }

    public void clickSave() {
        WaitUtils.waitForClickability(saveButton).click();
    }

    // --- Getters for validations ---
    public String getFullName() {
        return fullNameInput.getAttribute("value");
    }

    public String getEmail() {
        return emailInput.getAttribute("value");
    }

    public String getBio() {
        return bioInput.getAttribute("value");
    }

    public boolean isSaveButtonEnabled() {
        return saveButton.isEnabled();
    }

    public boolean isSuccessNotificationVisible() {
        return isElementVisible(successNotification);
    }

    public boolean isAccountInfoHeaderVisible() {
        return isElementVisible(accountInfoHeader);
    }
}
