package org.noir.guice.boot.executor.binder.impl;

import java.lang.reflect.Type;
import java.util.List;

import com.google.inject.Binder;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.name.Names;

import org.noir.guice.boot.annotations.Injectable;
import org.noir.guice.boot.executor.binder.ClassBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instance binder
 * 
 * 默认所有注入到Guice容器的实例将通过该binder进行，实现
 * 了自动的接口注入填充;重复接口实现填充Named;
 */
public class InstanceBinder implements ClassBinder {
    private static final Logger logger = LoggerFactory.getLogger(BeforeBindPostProcessorBinder.class);

    @Override
    public void apply(List<Class<?>> classes, Binder binder) {
        // TODO: 实现接口绑定与冲突实现的自动naming
        for (Class<?> clazz : classes) {
            Type[] genericInterfaces = clazz.getGenericInterfaces();
            Injectable annotation = clazz.getAnnotation(Injectable.class);
            for (Type genericInterface : genericInterfaces) {
                AnnotatedBindingBuilder builder = binder.bind((TypeLiteral) TypeLiteral.get(genericInterface));
                LinkedBindingBuilder linkedBindingBuilder = builder;
                if (!annotation.named().equals("")) {
                    linkedBindingBuilder = builder.annotatedWith(Names.named(annotation.named()));
                }
                linkedBindingBuilder.to(clazz).in(Scopes.SINGLETON);
                logger.info("Bond injectable: {} to {}", genericInterface.getTypeName(), clazz.getName());
            }
            logger.info("Bond injectable: {}", clazz.getName());
        }

    }
    
}
