package com.skillboost.ui_tests.pages;



import com.skillboost.ui_tests.base.UiBasePage;
import com.skillboost.ui_tests.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object Model for the SkillBoost Skill Arena Page.
 * Represents the "Skill Arena - Coming Soon" header and the navigation button back to Dashboard.
 */
public class SkillArenaPage extends UiBasePage {

    // --- Constructor ---
    public SkillArenaPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ============================================================
    //                       PAGE ELEMENTS
    // ============================================================

    @FindBy(xpath = "//h1[normalize-space()='Skill Arena - Coming Soon']")
    private WebElement skillArenaHeader;

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

    public boolean isSkillArenaHeaderVisible() {
        return isElementVisible(skillArenaHeader);
    }

    public boolean isBackToDashboardButtonVisible() {
        return isElementVisible(backToDashboardButton);
    }
}
