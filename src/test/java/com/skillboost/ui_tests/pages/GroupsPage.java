package com.skillboost.ui_tests.pages;



import com.skillboost.ui_tests.base.UiBasePage;
import com.skillboost.ui_tests.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object Model for the SkillBoost Groups Page.
 * Represents the "Groups - Coming Soon" header and navigation button back to Dashboard.
 */
public class GroupsPage extends UiBasePage {

    // --- Constructor ---
    public GroupsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ============================================================
    //                       PAGE ELEMENTS
    // ============================================================

    @FindBy(xpath = "//h1[normalize-space()='Groups - Coming Soon']")
    private WebElement groupsHeader;

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

    public boolean isGroupsHeaderVisible() {
        return isElementVisible(groupsHeader);
    }

    public boolean isBackToDashboardButtonVisible() {
        return isElementVisible(backToDashboardButton);
    }
}
