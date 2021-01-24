package org.noir.guice.boot.executor.binder.impl;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.inject.Binder;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import org.noir.guice.boot.annotations.Injectable;
import org.noir.guice.boot.executor.binder.ClassBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instance binder
 * <p>
 * 默认所有注入到Guice容器的实例将通过该binder进行，实现了自动的接口注入填充;重复接口实现填充Named;
 */
public class InstanceBinder implements ClassBinder {
    private static final Logger logger = LoggerFactory.getLogger(BeforeBindPostProcessorBinder.class);

    private final Map<Class<?>, List<Class<?>>> genericInterfaceImplMap = new HashMap<>(256);

    @Override
    public void apply(List<Class<?>> classes, Binder binder) {
        classes.forEach(this::analysisClassGeneric);
        bindGenericClass(binder);
    }

    /**
     * Analysis class generic meta info to impl map
     *
     * @param clazz target class
     */
    private void analysisClassGeneric(Class<?> clazz) {
        logger.info("Bond injectable: {}", clazz.getName());
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        for (Type type : genericInterfaces) {
            if (!(type instanceof Class)) {
                continue;
            }
            if (!genericInterfaceImplMap.containsKey(type)) {
                List<Class<?>> container = new ArrayList<>();
                genericInterfaceImplMap.put((Class<?>) type, container);
            }
            genericInterfaceImplMap.get(type).add(clazz);
        }
    }

    /**
     * Binding reality logic
     *
     * @param binder binding handle
     */
    private void bindGenericClass(Binder binder) {
        for (var entry : genericInterfaceImplMap.entrySet()) {
            // Only singleton impl will be auto bond to SINGLETON scope,
            // when is have impls will be bond to multiple container, it
            // mean Set<Basic> and Map<name, implInstance> will be set.
            if (entry.getValue().size() > 1) {
                bindMultiTarget(binder, entry.getKey(), entry.getValue());
            } else {
                bindSingletonTarget(binder, entry.getKey(), entry.getValue().get(0));
            }
        }
    }

    /**
     * Singleton binder
     * Instance will be auto construct with guice, but we will named it
     * other inject to container, that name means: annotation named,class
     * simple named,class full named.
     *
     * @param binder handle
     * @param type   generic type
     * @param target target class
     */
    private void bindSingletonTarget(Binder binder, Type type, Class<?> target) {
        Injectable injectableAnt = target.getAnnotation(Injectable.class);
        String simpleName = target.getSimpleName();
        String name = target.getName();
        String antName = injectableAnt.named();
        bindSingletonTarget(binder, type, target, name);
        bindSingletonTarget(binder, type, target, simpleName);
        bindSingletonTarget(binder, type, target, antName);
    }

    /**
     * Singleton binding reality logic
     * That is guice adaptor,check the name and binding class to
     * the Singleton Scope.
     *
     * @param binder handle
     * @param type   generic type
     * @param target target class
     * @param name   named
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void bindSingletonTarget(Binder binder, Type type, Class<?> target, String name) {
        if (Strings.isNullOrEmpty(name)) {
            return;
        }
        binder.bind(TypeLiteral.get(type))
                .annotatedWith(Names.named(name))
                .to((Class) target)
                .in(Scopes.SINGLETON);
        logger.info("Bond Singleton container {} to {} with {}", type.getTypeName(), target.getName(), name);
    }

    /**
     * Multiple binding reality logic
     * That is guice adaptor of Multibinder and MapBinder.In the
     * map binder, instance will be set to Map[key, GenericType],
     * key will be class simple name, when the impl repeated,the
     * key will be class full name, btw, you can customize name
     * with @Injectable annotation.
     *
     * @param binder  handle
     * @param type    generic type
     * @param targets target class
     * @see com.google.inject.multibindings.Multibinder
     * @see com.google.inject.multibindings.MapBinder
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void bindMultiTarget(Binder binder, Class<?> type, List<Class<?>> targets) {
        Multibinder multibinder = Multibinder.newSetBinder(binder, type);
        for (var target : targets) {
            multibinder.addBinding().to(target);
            logger.info("Bond Multi container {} to {}", type.getTypeName(), target.getName());
        }
        MapBinder stringMapBinder = MapBinder.newMapBinder(binder, String.class, type);
        Map<String, Integer> nameCounterMap = new HashMap<>();
        targets.forEach(item -> {
            int count = nameCounterMap.getOrDefault(item.getSimpleName(), 0);
            nameCounterMap.put(item.getSimpleName(), count + 1);
        });
        for (var target : targets) {
            String key = target.getAnnotation(Injectable.class).named();
            if (Strings.isNullOrEmpty(key)) {
                key = target.getSimpleName();
                if (nameCounterMap.get(key) > 1) {
                    key = target.getName();
                }
            }
            stringMapBinder.addBinding(key).to(target);
            logger.info("Bond Map container {} to {} with {}", type.getTypeName(), target.getName(), key);
        }
    }

}
