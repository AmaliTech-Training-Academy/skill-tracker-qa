package com.skillboost.api_test.models.user.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignores any unexpected fields
public class RegisterResponse {

    // Success response fields
    private Boolean success;
    private String message;
    private UserData data;

    // Error response fields
    private Integer status;
    private String detail;
    private String instance;

    // Validation error list (only appears on 400)
    private List<ErrorItem> errors;

    // Custom Metadata class
    private Metadata metadata;
}
