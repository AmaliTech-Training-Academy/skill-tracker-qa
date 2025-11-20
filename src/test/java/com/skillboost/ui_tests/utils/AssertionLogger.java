package com.skillboost.ui_tests.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Utility for logging assertions with context.
 * Ensures all test validations are traceable in logs and reports.
 */
public final class AssertionLogger {

    private static final Logger log = LoggerFactory.getLogger(AssertionLogger.class);

    private AssertionLogger() {
        // Prevent instantiation
    }

    /**
     * Assert that two objects are equal, with logging.
     */
    public static <T> void assertEqualsWithLog(T expected, T actual, String message) {
        log.info("ASSERT EQUALS: {} | Expected='{}' | Actual='{}'", message, expected, actual);
        assertEquals(expected, actual, message);
        log.info("✔ PASSED: {}", message);
    }

    /**
     * Assert that two objects are not equal, with logging.
     */
    public static <T> void assertNotEqualsWithLog(T unexpected, T actual, String message) {
        log.info("ASSERT NOT EQUALS: {} | Unexpected='{}' | Actual='{}'", message, unexpected, actual);
        assertNotEquals(unexpected, actual, message);
        log.info("✔ PASSED: {}", message);
    }

    /**
     * Assert that a condition is true, with logging.
     */
    public static void assertTrueWithLog(boolean condition, String message) {
        log.info("ASSERT TRUE: {}", message);
        assertTrue(condition, message);
        log.info("✔ PASSED: {}", message);
    }

    /**
     * Assert that a condition is false, with logging.
     */
    public static void assertFalseWithLog(boolean condition, String message) {
        log.info("ASSERT FALSE: {}", message);
        assertFalse(condition, message);
        log.info("✔ PASSED: {}", message);
    }

    /**
     * Assert that an object is null, with logging.
     */
    public static void assertNullWithLog(Object object, String message) {
        log.info("ASSERT NULL: {} | Value='{}'", message, object);
        assertNull(object, message);
        log.info("✔ PASSED: {}", message);
    }

    /**
     * Assert that an object is not null, with logging.
     */
    public static void assertNotNullWithLog(Object object, String message) {
        log.info("ASSERT NOT NULL: {} | Value='{}'", message, object);
        assertNotNull(object, message);
        log.info("✔ PASSED: {}", message);
    }

    /**
     * Assert that a string contains another substring, with logging.
     */
    public static void assertContainsWithLog(String actual, String expectedSubstring, String message) {
        log.info("ASSERT CONTAINS: {} | Expected substring='{}' | Actual='{}'", message, expectedSubstring, actual);
        assertTrue(actual.contains(expectedSubstring),
                String.format("%s | Expected substring '%s' not found in '%s'", message, expectedSubstring, actual));
        log.info("✔ PASSED: {}", message);
    }
}