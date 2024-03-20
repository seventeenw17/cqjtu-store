package com.backend.springstore.user.pojo.dto;

import lombok.Data;

@Data
public class UserInsertDTO {
    private String username;
    private String password;
    private String phone;
    private String email;
    private Integer gender;
}
