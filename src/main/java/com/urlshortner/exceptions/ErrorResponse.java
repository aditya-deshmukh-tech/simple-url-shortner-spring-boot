package com.urlshortner.exceptions;

import java.util.ArrayList;
import java.util.List;

public final class ErrorResponse {

    private int errorCode;
    private String message;
    private List<String> detail = new ArrayList<>();

    //getters
    public int getErrorCode() {
        return errorCode;
    }


    public String getMessage() {
        return message;
    }


    public List<String> getDetail() {
        return detail;
    }

    //builder class to build response
    public static class ErrorResponseBuilder {

        private ErrorResponse errorResponseToBuild;

        public ErrorResponseBuilder() {
            errorResponseToBuild = new ErrorResponse();
        }

        public ErrorResponseBuilder withErrorCode(int errorCode) {
            this.errorResponseToBuild.errorCode = errorCode;
            return this;
        }

        public ErrorResponseBuilder withMessage(String message) {
            this.errorResponseToBuild.message = message;
            return this;
        }

        public ErrorResponseBuilder withDetail(String detail) {
            this.errorResponseToBuild.detail.add(detail);
            return this;
        }

        public ErrorResponse build() {
            ErrorResponse apiErrorResponse = errorResponseToBuild;
            errorResponseToBuild = new ErrorResponse();
            return apiErrorResponse;
        }

    }

}
