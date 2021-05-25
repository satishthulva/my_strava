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
public class UpdateUser {
    private long id;
    private String firstName;
    private String email;
    private boolean active;
}
