package com.satish.accesscontrol;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Annotation Utilities helpful implementing Role Based Access Control
 *
 * @author satish.thulva
 **/
@Slf4j
public class AnnotationUtils {

    /**
     * Find all instances of given annotation searching all superClasses and interfaces hierarchy of Method
     */
    public static <T extends Annotation> List<T> getAnnotations(Method method, Class<T> annotation) {
        String methodName = method.getName();
        Class<?> clz = method.getDeclaringClass();
        return Stream.concat(
                Stream.of(clz),
                Stream.concat(
                        Stream.of(getAllSuperClasses(clz)),
                        Stream.of(getAllInterfaces(clz))))
                .map(c -> {
                    try {
                        return c.getMethod(methodName, method.getParameterTypes());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(m -> m.getAnnotation(annotation))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Find all super classes of given class
     */
    private static Class<?>[] getAllSuperClasses(Class<?> clz) {
        List<Class<?>> list = new ArrayList<>();
        while ((clz = clz.getSuperclass()) != null) {
            list.add(clz);
        }
        return list.toArray(new Class<?>[list.size()]);
    }

    /**
     * Find all interfaces implemented by given class
     */
    private static Class<?>[] getAllInterfaces(Class<?> clz) {
        HashSet<Class<?>> set = new HashSet<>();
        getAllInterfaces(clz, set);
        return set.toArray(new Class<?>[set.size()]);
    }

    private static void getAllInterfaces(Class<?> clz, Set<Class<?>> visited) {
        if (clz.getSuperclass() != null) {
            getAllInterfaces(clz.getSuperclass(), visited);
        }
        for (Class<?> c : clz.getInterfaces()) {
            if (visited.add(c)) {
                getAllInterfaces(c, visited);
            }
        }
    }
}
