package com.skillboost.ui_tests.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class for reading test data JSON files located in:
 * src/test/resources/ui/testdata/
 *
 * Supports retrieving values by key, mapping to POJOs,
 * or returning full nested structures.
 */
public final class JsonDataReader {

    private static final String TESTDATA_PATH = "src/test/resources/ui/testdata/";
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonDataReader() {
        // Prevent instantiation
    }

    private static void validateFileExtension(String fileName) {
        if (!fileName.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException("Only .json files are supported: " + fileName);
        }
    }

    /**
     * Reads a JSON file and returns the root as a JsonNode.
     */
    public static JsonNode readJson(String fileName) {
        validateFileExtension(fileName);
        try {
            File file = new File(TESTDATA_PATH + fileName);
            return mapper.readTree(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + fileName, e);
        }
    }

    /**
     * Reads a JSON file and maps it to a custom POJO class.
     */
    public static <T> T readJsonAsObject(String fileName, Class<T> clazz) {
        validateFileExtension(fileName);
        try {
            File file = new File(TESTDATA_PATH + fileName);
            return mapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to map JSON to object: " + fileName, e);
        }
    }

    /**
     * Reads a test data file and returns it as a nested Map structure.
     * Useful for multi-scenario test data.
     *
     * Example JSON:
     * {
     *   "validUser": { "email": "test@skillboost.com", "password": "Pass123" },
     *   "invalidUser": { "email": "bad@skillboost.com", "password": "wrong" }
     * }
     */
    public static Map<String, Map<String, String>> getTestData(String fileName) {
        validateFileExtension(fileName);
        try {
            File file = new File(TESTDATA_PATH + fileName);
            return mapper.readValue(file, new TypeReference<Map<String, Map<String, String>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON test data: " + fileName, e);
        }
    }

    /**
     * Retrieves a nested value from a JSON file using dot notation.
     * Example:
     *   getValue("login-data.json", "validUser.email")
     */
    public static String getValue(String fileName, String keyPath) {
        validateFileExtension(fileName);
        JsonNode node = readJson(fileName);

        String[] keys = keyPath.split("\\.");
        for (String key : keys) {
            node = node.path(key);
        }

        if (node.isMissingNode() || node.isNull()) {
            throw new RuntimeException("Key not found in " + fileName + ": " + keyPath);
        }

        return node.asText();
    }

    /**
     * Logs all top-level keys in a given JSON file (useful for debugging test data).
     */
    public static void listKeys(String fileName) {
        JsonNode node = readJson(fileName);
        Iterator<String> fieldNames = node.fieldNames();
        System.out.println("Available keys in " + fileName + ":");
        while (fieldNames.hasNext()) {
            System.out.println(" - " + fieldNames.next());
        }
    }
}

