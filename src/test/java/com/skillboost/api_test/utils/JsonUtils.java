package com.skillboost.api_test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads a JSON file and maps it to the given class type.
     *
     * @param filePath path to the JSON file relative to project root
     * @param clazz    class type to map JSON to
     * @param <T>      generic type
     * @return mapped object
     */
    public static <T> T fromJsonFile(String filePath, Class<T> clazz) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("JSON file not found: " + filePath);
            }
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read or parse JSON file: " + filePath, e);
        }
    }

    /**
     * Converts any object to its JSON string representation.
     *
     * @param obj object to convert
     * @return JSON string
     */
    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert object to JSON string", e);
        }
    }

    /**
     * Converts an object to a different type safely, avoiding unchecked casts.
     *
     * @param fromValue object to convert
     * @param toClass   target class type
     * @param <T>       target generic type
     * @return converted object of type T
     */
    public static <T> T convertValue(Object fromValue, Class<T> toClass) {
        return objectMapper.convertValue(fromValue, toClass);
    }
}
