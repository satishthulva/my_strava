package com.satish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author satish.thulva
 **/
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserCreds {
    private String email;
    private String password;
}
