package com.skillboost.api_test.endpoints;


import com.skillboost.api_test.models.user.request.RegisterRequest;
import com.skillboost.api_test.models.user.response.RegisterResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * UserEndpoints contains all API calls related to the User service.
 */
public class UserEndpoints {

    private final String baseUrl;

    public UserEndpoints(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Calls the Register endpoint.
     *
     * @param request RegisterRequest object
     * @return RegisterResponse mapped from API response
     */
    public RegisterResponse register(RegisterRequest request) {
        Response response = RestAssured
                .given()
                .baseUri(baseUrl)
                .basePath("/auth/register")
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post()
                .then()
                .extract().response();

        return response.as(RegisterResponse.class);
    }

    // Future endpoints (Login, UpdateProfile, etc.) can be added here
}
