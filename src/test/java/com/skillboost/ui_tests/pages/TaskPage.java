package com.skillboost.ui_tests.pages;



import com.skillboost.ui_tests.base.UiBasePage;
import com.skillboost.ui_tests.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object Model for the SkillBoost Task Page.
 * Represents headers, task sections, and card components for Today's and Previous tasks.
 */
public class TaskPage extends UiBasePage {

    // --- Constructor ---
    public TaskPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ============================================================
    //                       PAGE ELEMENTS
    // ============================================================

    // --- Main Header ---
    @FindBy(css = ".tasks-title")
    private WebElement tasksHeader;

    // --- Today's Tasks Section ---
    @FindBy(css = ".list-title.ng-star-inserted")
    private WebElement todaysTasksHeader;

    // First task card under Today's Tasks
    @FindBy(css = "body > app-root:nth-child(1) > app-dashboard:nth-child(2) > main:nth-child(1) > section:nth-child(2) > app-tasks-dashboard:nth-child(3) > section:nth-child(1) > app-task-list:nth-child(2) > section:nth-child(1) > div:nth-child(1) > div:nth-child(2) > app-tasks-card:nth-child(1) > div:nth-child(1)")
    private WebElement firstTaskCard;

    // --- Previous Tasks Section ---
    @FindBy(css = "h2[class='list-title']")
    private WebElement previousTasksHeader;


    // ============================================================
    //                       PAGE ACTIONS
    // ============================================================

    public void clickFirstTaskCard() {
        click(firstTaskCard);
    }


    // ============================================================
    //                       VALIDATIONS
    // ============================================================

    public boolean isTasksHeaderVisible() {
        return isElementVisible(tasksHeader);
    }

    public boolean isTodaysTasksHeaderVisible() {
        return isElementVisible(todaysTasksHeader);
    }

    public boolean isFirstTaskCardVisible() {
        return isElementVisible(firstTaskCard);
    }

    public boolean isPreviousTasksHeaderVisible() {
        scrollToElement(previousTasksHeader);
        return isElementVisible(previousTasksHeader);
    }
}
