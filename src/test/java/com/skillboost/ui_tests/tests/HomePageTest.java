package com.skillboost.ui_tests.tests;

import com.skillboost.ui_tests.base.UiBaseTest;
import com.skillboost.ui_tests.pages.HomePage;
import com.skillboost.ui_tests.pages.LoginPage;
import com.skillboost.ui_tests.pages.SignupPage;
import com.skillboost.ui_tests.utils.AssertionLogger;
import com.skillboost.ui_tests.utils.WaitUtils;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * UI Tests for the SkillBoost Homepage.
 * Validates navigation, element visibility, and button interactions.
 */
@Epic("SkillBoost Platform")
@Feature("Homepage Functionality")
@Severity(SeverityLevel.NORMAL)
public class HomePageTest extends UiBaseTest {

    @Test
    @Story("Homepage loads with visible logo and main CTA buttons")
    @DisplayName("Verify homepage elements are visible on load")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures key homepage elements such as logo, Start Practicing and Learn More buttons are visible when the page loads.")
    public void verifyHomepageLogoVisible() {
        HomePage homePage = new HomePage(driver);

        boolean isLogoVisible = homePage.isLogoDisplayed();
        AssertionLogger.assertTrueWithLog(isLogoVisible, "Logo is visible on homepage");

    }

    @Test
    @Story("Homepage navigation buttons are interactive")
    @DisplayName("Verify 'Start Practicing' button is clickable on homepage")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures the 'Start Practicing' button is visible and can be clicked successfully from the homepage.")
    public void verifyStartPracticingButtonClickable() {
        HomePage homePage = new HomePage(driver);

        // Step 2: Click the button
        homePage.clickStartPracticing();
        AssertionLogger.assertTrueWithLog(true, "'Start Practicing' button clicked successfully");
    }


    @Test
    @Story("Homepage navigation")
    @DisplayName("Verify 'Start Practicing' navigates to Signup Page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that clicking the 'Start Practicing' button on the homepage redirects the user to the signup page.")
    public void verifyStartPracticingNavigatesToSignupPage() {
        HomePage homePage = new HomePage(driver);
        SignupPage signupPage = new SignupPage(driver);


        // Step 1: Verify it’s visible and ready
        boolean isButtonVisible = homePage.isStartPracticingButtonVisible();
        AssertionLogger.assertTrueWithLog(isButtonVisible, "'Start Practicing' button is visible on homepage");

        // Step 2: Click the button
        homePage.clickStartPracticing();
        log.info("Clicked 'Start Practicing' button, waiting for Signup page...");

        // Step 3: Wait for the Signup Page to load
        WaitUtils.waitForVisibility(signupPage.createAccountHeader);

        // Step 4: Validate that user is on Signup Page
        boolean isSignupHeaderVisible = signupPage.isCreateAccountHeaderVisible();
        AssertionLogger.assertTrueWithLog(isSignupHeaderVisible, "User is successfully navigated to Signup page");
    }


    @Test
    @Story("Homepage content visibility")
    @DisplayName("Verify pricing options are visible after scrolling down the homepage")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that the 'SkillDev Pricing Models' section becomes visible when the user scrolls down on the homepage.")
    public void verifyPricingOptionsVisibleAfterScroll() {
        HomePage homePage = new HomePage(driver);

        // Step 1: Wait until the homepage is ready (e.g., logo visible)
        boolean isLogoVisible = homePage.isLogoDisplayed();
        AssertionLogger.assertTrueWithLog(isLogoVisible, "Homepage loaded successfully, logo is visible");

        // Step 2: Scroll to pricing options section
        homePage.scrollToPricingOptions();
        log.info("Scrolled to the 'SkillDev Pricing Models' section.");

        // Step 3: Verify the pricing section is visible
        boolean isPricingVisible = homePage.isPricingOptionsVisible();
        AssertionLogger.assertTrueWithLog(isPricingVisible, "'SkillDev Pricing Models' section is visible after scrolling");
    }

}
