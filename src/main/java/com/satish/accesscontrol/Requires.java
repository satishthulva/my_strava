package com.satish.accesscontrol;

import com.satish.datamodels.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify Access Control restrictions for a method
 *
 * @author satish.thulva
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requires {
    Role value();
}
