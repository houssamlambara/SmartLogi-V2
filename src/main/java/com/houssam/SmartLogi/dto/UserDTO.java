package com.houssam.SmartLogi.dto;

import com.houssam.SmartLogi.model.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String email;
    private Role role;


}
