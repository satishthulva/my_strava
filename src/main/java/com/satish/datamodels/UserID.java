package com.satish.datamodels;

import com.satish.accesscontrol.AccessControlled;
import com.satish.accesscontrol.Flavour;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author satish.thulva
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserID implements AccessControlled {
    private long id;

    @Override
    public Flavour getFlavour() {
        return Flavour.USER;
    }
}
