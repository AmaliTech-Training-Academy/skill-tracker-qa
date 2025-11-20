package com.skillboost.api_test.models.user.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignores any unexpected fields from API
public class UserData {
    private String id;
    private String email;
    private String username;
    private String role;
    private String state;
    private String tourStatus;

    @JsonProperty("is_verified") // Maps the JSON field "is_verified" to this property
    private boolean verified;

    private String premiumTier;
    private String language;
    private String timezone;
}
