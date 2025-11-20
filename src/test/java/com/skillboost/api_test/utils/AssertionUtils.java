package com.skillboost.api_test.utils;


import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import com.skillboost.api_test.models.user.response.ErrorItem;

/**
 * Utility class for reusable assertions in API tests.
 */
public class AssertionUtils {

    /**
     * Asserts that a boolean condition is true.
     *
     * @param condition the condition to check
     * @param message   assertion message
     */
    public static void assertTrueCondition(boolean condition, String message) {
        assertTrue(condition, message);
    }

    /**
     * Asserts that two objects are equal.
     *
     * @param expected expected value
     * @param actual   actual value
     * @param message  assertion message
     */
    public static void assertEqualsValue(Object expected, Object actual, String message) {
        assertEquals(expected, actual, message);
    }

    /**
     * Asserts that an object is not null.
     *
     * @param obj     object to check
     * @param message assertion message
     */
    public static void assertNotNullValue(Object obj, String message) {
        assertNotNull(obj, message);
    }

    /**
     * Asserts that a list of errors contains a specific field.
     *
     * @param errors list of ErrorItem objects
     * @param field  the field to check
     */
    public static void assertErrorContainsField(List<ErrorItem> errors, String field) {
        boolean found = errors.stream().anyMatch(error -> error.getField().equals(field));
        assertTrue(found, "Expected error field not found: " + field);
    }

    /**
     * Suppresses verbose REST Assured logs in the terminal.
     * Call this method before executing the request if you want minimal output.
     */
    public static void suppressLogs() {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    }
}
