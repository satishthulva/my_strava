package com.satish.accesscontrol;

import com.satish.datamodels.Role;
import com.satish.datamodels.UserID;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jvnet.hk2.annotations.Service;

import java.lang.reflect.Method;

/**
 * Interceptor to enforce Role Based Access control. If access is not allowed, an exception
 * {@link AccessControlException} will be thrown
 *
 * @author satish.thulva
 **/
@Slf4j
@Service
public class AccessControlAOPInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        String methodName = method.getName();

        Requires annotation = AnnotationUtils.getAnnotations(method, Requires.class).get(0);
        Object[] arguments = methodInvocation.getArguments();
        Actor actor = getActor(arguments);
        if (actor == null) {
            log.info("Actor not found for method invocation {}. Not allowing access. Parameters are {}",
                    methodName, arguments);
            throw new AccessControlException("Actor not found");
        }

        Role requiredRole = annotation.value();
        // Assumption here is all roles have a priority and all things a low priority user can do can be done
        // by all the high priority users as well
        if (actor.getRole().getPriority() >= requiredRole.getPriority()) {
            return methodInvocation.proceed();
        } else {
            log.info("Method {} requires {}. But actor {} has role {}. Checking if this is user self resource",
                    methodName, requiredRole, actor.getId(), actor.getRole());
            if (isSelfUserResource(method, actor, arguments)) {
                log.info("Allowing {} for user {} as it is a self user resource operation", methodName, actor.getId());
                return methodInvocation.proceed();
            }

            log.info("Not allowing access to {} for {} ", methodName, actor.getId());
            throw new AccessControlException("Not allowing access to " + methodName + " for " + actor.getId());
        }
    }

    private boolean isSelfUserResource(Method method, Actor actor, Object[] arguments) {
        if (AnnotationUtils.getAnnotations(method, SelfAccess.class).size() > 0) {
            for (Object arg : arguments) {
                if (arg instanceof UserID) {
                    UserID userID = (UserID) arg;
                    return userID.getId() == actor.getId();
                }
            }
        }

        return false;
    }

    private Actor getActor(Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Actor) {
                return (Actor) arg;
            }
        }

        return null;
    }

}
