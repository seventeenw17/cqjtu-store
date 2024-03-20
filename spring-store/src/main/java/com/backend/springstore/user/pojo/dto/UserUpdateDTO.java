package com.backend.springstore.user.pojo.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private Integer id;
    private String username;
    private String phone;
    private String email;
    private Integer gender;

}
