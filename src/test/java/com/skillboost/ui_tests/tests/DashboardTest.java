package com.skillboost.ui_tests.tests;



import com.skillboost.ui_tests.base.UiBaseTest;
import com.skillboost.ui_tests.pages.DashboardPage;
import com.skillboost.ui_tests.pages.HomePage;
import com.skillboost.ui_tests.pages.LoginPage;
import com.skillboost.ui_tests.utils.AssertionLogger;
import com.skillboost.ui_tests.utils.JsonDataReader;
import com.skillboost.ui_tests.utils.WaitUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class DashboardTest extends UiBaseTest {

    private static final String TESTDATA_FILE = "login-data.json";

    /**
     * Helper method to login with valid credentials
     */
    private DashboardPage loginAndNavigateToDashboard() {
        HomePage homePage = new HomePage(driver);
        homePage.clickLoginButton();
        WaitUtils.waitForUrlContains("/login");

        LoginPage loginPage = new LoginPage(driver);
        WaitUtils.waitForVisibility(loginPage.getLogo());
        WaitUtils.waitForVisibility(loginPage.getWelcomeBackText());

        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        String email = testData.get("validUser").get("email");
        String password = testData.get("validUser").get("password");

        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        DashboardPage dashboardPage = new DashboardPage(driver);
        WaitUtils.waitForVisibility(dashboardPage.getLogoElement());
        return dashboardPage;
    }

    @Test
    @Story("Dashboard UI")
    @DisplayName("Verify all main elements are visible on the Dashboard")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that logo, welcome text, profile icon, progress bar, and progress graph are visible after login.")
    public void verifyDashboardElementsAreVisible() {
        DashboardPage dashboardPage = loginAndNavigateToDashboard();

        AssertionLogger.assertTrueWithLog(dashboardPage.isLogoVisible(), "SkillDev logo is visible");
        AssertionLogger.assertTrueWithLog(dashboardPage.isWelcomeTextVisible(), "Welcome text is visible");
    }

    @Test
    @Story("Dashboard Navigation")
    @DisplayName("Verify clicking Profile icon navigates to Settings page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Clicking on profile icon should navigate user to the settings page.")
    public void verifyProfileNavigation() {
        DashboardPage dashboardPage = loginAndNavigateToDashboard();

        dashboardPage.openProfile();
        WaitUtils.waitForUrlContains("/profile");

        AssertionLogger.assertTrueWithLog(driver.getCurrentUrl().contains("/profile"),
                "URL contains '/profile' after clicking profile icon");
    }

    @Test
    @Story("Dashboard Sidebar - Tasks")
    @DisplayName("Verify Task menu navigates to Task page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Clicking the Task menu should navigate to the Tasks page.")
    public void verifyTaskMenuNavigation() {
        DashboardPage dashboardPage = loginAndNavigateToDashboard();
        dashboardPage.clickTasks();
        WaitUtils.waitForUrlContains("/tasks");
        AssertionLogger.assertTrueWithLog(driver.getCurrentUrl().contains("/tasks"), "Task page loaded successfully");
    }

    @Test
    @Story("Dashboard Sidebar - Leaderboard")
    @DisplayName("Verify Leaderboard menu navigates to Leaderboard page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Clicking the Leaderboard menu should navigate to the Leaderboard page.")
    public void verifyLeaderboardMenuNavigation() {
        DashboardPage dashboardPage = loginAndNavigateToDashboard();
        dashboardPage.clickLeaderboard();
        WaitUtils.waitForUrlContains("/leaderboard");
        AssertionLogger.assertTrueWithLog(driver.getCurrentUrl().contains("/leaderboard"), "Leaderboard page loaded successfully");
    }

    @Test
    @Story("Dashboard Sidebar - Skill Arena")
    @DisplayName("Verify Skill Arena menu navigates to Skill Arena page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Clicking the Skill Arena menu should navigate to the Skill Arena page.")
    public void verifySkillArenaMenuNavigation() {
        DashboardPage dashboardPage = loginAndNavigateToDashboard();
        dashboardPage.clickSkillArena();
        WaitUtils.waitForUrlContains("/skill-arena");
        AssertionLogger.assertTrueWithLog(driver.getCurrentUrl().contains("/skill-arena"), "Skill Arena page loaded successfully");
    }

    @Test
    @Story("Dashboard Sidebar - Groups")
    @DisplayName("Verify Groups menu navigates to Groups page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Clicking the Groups menu should navigate to the Groups page.")
    public void verifyGroupsMenuNavigation() {
        DashboardPage dashboardPage = loginAndNavigateToDashboard();
        dashboardPage.clickGroups();
        WaitUtils.waitForUrlContains("/groups");
        AssertionLogger.assertTrueWithLog(driver.getCurrentUrl().contains("/groups"), "Groups page loaded successfully");
    }

    @Test
    @Story("Dashboard Sidebar - Settings")
    @DisplayName("Verify Settings menu navigates to Settings page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Clicking the Settings menu should navigate to the Settings page.")
    public void verifySettingsMenuNavigation() {
        DashboardPage dashboardPage = loginAndNavigateToDashboard();
        dashboardPage.openSettings();
        WaitUtils.waitForUrlContains("/settings");
        AssertionLogger.assertTrueWithLog(driver.getCurrentUrl().contains("/settings"), "Settings page loaded successfully");
    }

}
