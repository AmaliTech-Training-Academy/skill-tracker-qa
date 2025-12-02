package com.skillboost.ui_tests.pages;

import com.skillboost.ui_tests.base.UiBasePage;
import com.skillboost.ui_tests.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object Model for the SkillBoost Dashboard.
 * Encapsulates web elements and user interactions for navigation and visibility checks.
 */
public class DashboardPage extends UiBasePage {

    // --- Constructor ---
    public DashboardPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // --- Header Elements ---
    @FindBy(css = ".sidebar-logo")
    private WebElement skilldevLogo;

    @FindBy(css = "header[class='dashboard-header'] h1")
    private WebElement welcomeText;

    @FindBy(css = ".navbar-profile--name")
    private WebElement profileIcon;

    // --- Sidebar Menu Elements ---
    @FindBy(css = "a[data-tour-id='sidebar-tasks']")
    private WebElement taskMenu;

    @FindBy(css = "a[data-tour-id='sidebar-leaderboard'] span")
    private WebElement leaderboardMenu;

    @FindBy(css = "a[data-tour-id='sidebar-skill-arena'] span")
    private WebElement skillArenaMenu;

    @FindBy(css = "a[data-tour-id='sidebar-groups']")
    private WebElement groupsMenu;

    @FindBy(css = ".sidebar-footer-item")
    private WebElement settingsMenu;

    @FindBy(xpath = "//button[normalize-space()='Logout']")
    private WebElement logoutButton;

    // --- Progress Elements ---
    @FindBy(css = ".progress-stats")
    private WebElement progressBar;

    @FindBy(css = ".tooltip-area.ng-tns-c2588001360-17")
    private WebElement progressGraph;


    // ============================================================
    //                       PAGE ACTIONS
    // ============================================================

    // --- Navigation Actions ---
    public void clickTasks() {
        click(taskMenu);
    }

    public void clickLeaderboard() {
        click(leaderboardMenu);
    }

    public void clickSkillArena() {
        click(skillArenaMenu);
    }

    public void clickGroups() {
        click(groupsMenu);
    }

    public void openSettings() {
        click(settingsMenu);
    }

    public void clickLogout() {
        click(logoutButton);
    }

    public void openProfile() {
        click(profileIcon);
    }


    // ============================================================
    //                       VALIDATIONS
    // ============================================================

    public boolean isLogoVisible() {
        return isElementVisible(skilldevLogo);
    }

    public boolean isWelcomeTextVisible() {
        return isElementVisible(welcomeText);
    }

    public boolean isProgressBarVisible() {
        scrollToElement(progressBar);
        return isElementVisible(progressBar);
    }

    public boolean isProgressGraphVisible() {
        scrollToElement(progressGraph);
        return isElementVisible(progressGraph);
    }

    // --- Getter Methods for WebElements (for waits or other interactions) ---
    public WebElement getLogoElement() {
        return skilldevLogo;
    }

    public WebElement getWelcomeTextElement() {
        return welcomeText;
    }

    public WebElement getProgressBarElement() {
        return progressBar;
    }

    public WebElement getProgressGraphElement() {
        return progressGraph;
    }

}
