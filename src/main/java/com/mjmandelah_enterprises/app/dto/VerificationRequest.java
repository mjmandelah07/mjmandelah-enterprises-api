package com.mjmandelah_enterprises.app.dto;

import javax.validation.constraints.NotBlank;

public class VerificationRequest {
    @NotBlank(message = "Verification code is required")
    private String code;

    public VerificationRequest() {
    }

    public VerificationRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
