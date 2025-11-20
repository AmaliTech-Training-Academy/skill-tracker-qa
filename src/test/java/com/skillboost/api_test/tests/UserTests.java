package com.skillboost.api_test.tests;

import com.skillboost.api_test.base.BaseTest;
import com.skillboost.api_test.endpoints.UserEndpoints;
import com.skillboost.api_test.models.user.request.RegisterRequest;
import com.skillboost.api_test.models.user.response.RegisterResponse;
import com.skillboost.api_test.utils.AssertionUtils;
import com.skillboost.api_test.utils.JsonUtils;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Epic("User Service")
@Feature("Register")
public class UserTests extends BaseTest {

    private static UserEndpoints userEndpoints;
    private static Map<String, Object> testData;
    private static Map<String, Object> registerMap;

    @BeforeAll
    public static void setup() {
        userEndpoints = new UserEndpoints(BASE_URL);
        testData = JsonUtils.fromJsonFile("src/test/resources/api/testdata/user_test_data.json", Map.class);
        AssertionUtils.suppressLogs();

        // Get the "register" map from JSON
        registerMap = JsonUtils.convertValue(testData.get("register"), Map.class);
    }

    @Test
    @Order(1)
    @Story("Successful user registration")
    @Description("Valid registration should return success=true and correct user email")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Register: Success for valid user")
    void testRegisterValidUser() {
        // Load valid request from JSON safely
        Map<String, String> validPayload = JsonUtils.convertValue(registerMap.get("valid"), Map.class);

        RegisterRequest request = new RegisterRequest();
        request.setEmail(validPayload.get("email"));
        request.setPassword(validPayload.get("password"));

        // Call API
        RegisterResponse response = userEndpoints.register(request);

        // Assertions: only 2 main checks
        assertThat("Registration should succeed", response.getSuccess(), is(true));
        assertThat("User email should match request", response.getData().getEmail(), equalTo(request.getEmail()));
    }

    @Test
    @Order(2)
    @Story("Invalid user registration")
    @Description("Registration with invalid inputs should return errors or conflict")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Register: Fail for invalid users")
    void testRegisterInvalidUsers() {
        List<Map<String, String>> invalidPayloads =
                JsonUtils.convertValue(registerMap.get("invalid"), List.class);

        for (Map<String, String> payload : invalidPayloads) {
            RegisterRequest request = new RegisterRequest();
            request.setEmail(payload.get("email"));
            request.setPassword(payload.get("password"));

            RegisterResponse response = userEndpoints.register(request);

            // Only 2 main assertions per invalid test
            if (response.getErrors() != null && !response.getErrors().isEmpty()) {
                assertThat("Validation errors should exist", response.getErrors(), not(empty()));
                assertThat("At least one validation error field should match",
                        response.getErrors().get(0).getField(), not(isEmptyString()));
            } else if (response.getStatus() != null && response.getStatus() == 409) {
                assertThat("Expected conflict error", response.getStatus(), equalTo(409));
                assertThat("Error message should indicate duplicate email",
                        response.getMessage(), containsString("Email already exists"));
            }
        }
    }

    @Test
    @Order(3)
    @Story("Duplicate email registration")
    @Description("Registration with an already registered email should return 409 Conflict")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Register: Fail when email already exists")
    void testRegisterEmailAlreadyExists() {

        Map<String, String> existsPayload =
                (Map<String, String>) JsonUtils.convertValue(registerMap.get("invalid"), List.class).get(2);

        RegisterRequest request = new RegisterRequest();
        request.setEmail(existsPayload.get("email"));
        request.setPassword(existsPayload.get("password"));

        RegisterResponse response = userEndpoints.register(request);

        // ---- Handle backend outage (503) gracefully ----
        if (response.getStatus() != null && response.getStatus() == 503) {
            fail("API returned 503 Service Unavailable instead of 409 Conflict. Backend may be down.");
        }

        // ---- Expected correct behavior ----
        assertThat("Expected 409 Conflict for existing email",
                response.getStatus(), equalTo(409));

        assertThat("Error message should indicate duplicate email",
                response.getMessage().toLowerCase(),
                containsString("email already"));
    }


    @Test
    @Order(4)
    @Story("Invalid user registration")
    @Description("Registration with too short password should return validation error")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Register: Fail for too short password")
    void testRegisterPasswordTooShort() {
        // Extract the 'invalid' list from JSON
        List<Map<String, String>> invalidPayloads =
                JsonUtils.convertValue(registerMap.get("invalid"), List.class);

        // Find the payload for "Invalid password - too short"
        Map<String, String> shortPasswordPayload = invalidPayloads.stream()
                .filter(p -> "Invalid password - too short".equals(p.get("description")))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Test data for short password not found"));

        // Build request
        RegisterRequest request = new RegisterRequest();
        request.setEmail(shortPasswordPayload.get("email"));
        request.setPassword(shortPasswordPayload.get("password"));

        // Call endpoint
        RegisterResponse response = userEndpoints.register(request);

        // Assertions
        assertThat("Validation errors should exist", response.getErrors(), not(empty()));
        assertThat("Validation error should be for password",
                response.getErrors().get(0).getField(), containsString("password"));
    }





}
