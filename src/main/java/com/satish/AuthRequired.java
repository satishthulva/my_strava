package com.satish;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to mark REST resources or specific methods for authentication
 *
 * @author satish.thulva
 **/
@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface AuthRequired {}