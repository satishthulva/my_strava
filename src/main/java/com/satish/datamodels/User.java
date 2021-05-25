package com.satish.datamodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private UserID id;
    private String firstName;
    private String email;
    private Role role;
    private boolean active;
}
