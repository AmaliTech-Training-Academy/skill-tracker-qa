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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * UI Test for SkillBoost Logout functionality.
 */
@Tag("smoke")
public class LogoutTest extends UiBaseTest {

    private static final String TESTDATA_FILE = "login-data.json";

    @Test
    @Story("Logout Flow")
    @DisplayName("Verify that a valid user can log in and log out successfully")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Logs in using valid credentials, verifies dashboard is displayed, then logs out and verifies redirection to Login page.")
    public void verifyUserCanLoginAndLogout() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = new DashboardPage(driver);

        // Navigate to Login Page
        homePage.clickLoginButton();
        WaitUtils.waitForUrlContains("/login");

        // Load valid credentials
        Map<String, Map<String, String>> testData = JsonDataReader.getTestData(TESTDATA_FILE);
        String email = testData.get("validUser").get("email");
        String password = testData.get("validUser").get("password");

        // Enter credentials and log in
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        // Wait for dashboard
        WaitUtils.waitForVisibility(dashboardPage.getLogoElement());
        AssertionLogger.assertTrueWithLog(
                dashboardPage.isLogoVisible(),
                "Dashboard logo is visible – user has successfully logged in."
        );

        // Perform logout
        dashboardPage.clickLogout();

        // Verify redirection to login page
        WaitUtils.waitForUrlContains("/login");
        AssertionLogger.assertTrueWithLog(
                loginPage.isLogoVisible(),
                "Login page logo is visible – user has successfully logged out."
        );
    }
}
