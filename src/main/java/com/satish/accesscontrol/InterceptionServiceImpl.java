package com.satish.accesscontrol;

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.BuilderHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * Access control interceptor registration
 *
 * @author satish.thulva
 **/
public class InterceptionServiceImpl implements InterceptionService {
    private static final List<MethodInterceptor> INTERCEPTORS = Collections.singletonList(
            new AccessControlAOPInterceptor());

    @Override
    public Filter getDescriptorFilter() {
        return BuilderHelper.allFilter();
    }

    @Override
    public List<MethodInterceptor> getMethodInterceptors(Method method) {
        if (AnnotationUtils.getAnnotations(method, Requires.class).size() > 0) {
            return INTERCEPTORS;
        }

        return null;
    }

    @Override
    public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> constructor) {
        return null;
    }
}
