package com.skillboost.ui_tests.pages;



import com.skillboost.ui_tests.base.UiBasePage;
import com.skillboost.ui_tests.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object Model for the SkillBoost Leaderboard Page.
 * Represents the "Leaderboard - Coming Soon" header and the navigation button back to Dashboard.
 */
public class LeaderboardPage extends UiBasePage {

    // --- Constructor ---
    public LeaderboardPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ============================================================
    //                       PAGE ELEMENTS
    // ============================================================

    @FindBy(xpath = "//h1[normalize-space()='Leaderboard - Coming Soon']")
    private WebElement leaderboardHeader;

    @FindBy(xpath = "//a[normalize-space()='Back to Dashboard']")
    private WebElement backToDashboardButton;


    // ============================================================
    //                       PAGE ACTIONS
    // ============================================================

    public void clickBackToDashboard() {
        click(backToDashboardButton);
    }


    // ============================================================
    //                       VALIDATIONS
    // ============================================================

    public boolean isLeaderboardHeaderVisible() {
        return isElementVisible(leaderboardHeader);
    }

    public boolean isBackToDashboardButtonVisible() {
        return isElementVisible(backToDashboardButton);
    }
}
