package com.satish.accesscontrol;

/**
 * All access controlled resources will implement this interface
 *
 * @author satish.thulva
 **/
public interface AccessControlled {
    long getId();

    Flavour getFlavour();
}
