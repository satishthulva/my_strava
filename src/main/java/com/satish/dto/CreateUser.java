package com.satish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author satish.thulva
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateUser {
    private String name;
    private String email;
    private String password;

    // Optional fields that are needed only when Manager / Admin creates users
    private String role;
    private long createdBy;
}
