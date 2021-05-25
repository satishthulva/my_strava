package com.satish.accesscontrol;

import com.satish.datamodels.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author satish.thulva
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActorImpl implements Actor {
    private long id;
    private Role role;
}
