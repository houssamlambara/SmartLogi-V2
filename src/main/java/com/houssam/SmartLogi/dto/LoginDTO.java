package com.houssam.SmartLogi.dto;

import com.houssam.SmartLogi.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;

public class LoginDTO {

    @Data
    public static class Request {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class Response {
        private String token;
        private String email;
        private String roleName;
        private String userId;
    }
}
