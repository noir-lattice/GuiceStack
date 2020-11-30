package org.noir.guice.boot.executor.binder.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.inject.Binder;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import org.noir.guice.boot.annotations.Injectable;
import org.noir.guice.boot.executor.binder.ClassBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instance binder
 * 
 * 默认所有注入到Guice容器的实例将通过该binder进行，实现了自动的接口注入填充;重复接口实现填充Named;
 */
public class InstanceBinder implements ClassBinder {
    private static final Logger logger = LoggerFactory.getLogger(BeforeBindPostProcessorBinder.class);

    private final Map<Type, List<Class<?>>> genericInterfaceImplMap = new HashMap<>(256);

    @Override
    public void apply(List<Class<?>> classes, Binder binder) {
        classes.forEach(this::analysisClassGeneric);
        bindGenericClass(binder);
    }

    private void analysisClassGeneric(Class<?> clazz) {
        logger.info("Bond injectable: {}", clazz.getName());
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        for (Type type : genericInterfaces) {
            if (!genericInterfaceImplMap.containsKey(type)) {
                List<Class<?>> container = new ArrayList<>();
                genericInterfaceImplMap.put(type, container);
            }
            genericInterfaceImplMap.get(type).add(clazz);
        }
    }

    private void bindGenericClass(Binder binder) {
        for (var entry : genericInterfaceImplMap.entrySet()) {
            for (var target : entry.getValue()) {
                Injectable injectableAnt = target.getAnnotation(Injectable.class);
                String simpleName = target.getSimpleName();
                String name = target.getName();
                String antName = injectableAnt.named();
                bindTypeTargetWithName(binder, entry.getKey(), target, name);
                bindTypeTargetWithName(binder, entry.getKey(), target, simpleName);
                bindTypeTargetWithName(binder, entry.getKey(), target, antName);
            }
        }
    }

    private void bindTypeTargetWithName(Binder binder, Type type, Class<?> target, String name) {
        if (Strings.isNullOrEmpty(name)) {
            return;
        }
        binder.bind(TypeLiteral.get(type))
            .annotatedWith(Names.named(name))
            .to((Class) target)
            .in(Scopes.SINGLETON);
        logger.info("Bond {} to {} with {}", type.getTypeName(), target.getName(), name);
    }

}
