package com.skillboost.api_test.models.user.response;


import lombok.Data;

@Data
public class ErrorItem {
    private String field;
    private String message;
}
