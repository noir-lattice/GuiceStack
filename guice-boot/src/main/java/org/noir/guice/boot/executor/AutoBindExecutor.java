package org.noir.guice.boot.executor;

import com.google.inject.*;
import com.google.inject.Module;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.name.Names;
import org.noir.guice.boot.annotations.Injectable;
import org.noir.guice.boot.executor.func.AfterBindPostProcessor;
import org.noir.guice.boot.executor.func.BeforeBindPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;

public class AutoBindExecutor implements Module {
    private static final Logger logger = LoggerFactory.getLogger(AutoBindExecutor.class);

    private static class ClassContainer {
        private final List<Class<?>> clazzList;
        private final List<Class<BeforeBindPostProcessor>> beforeProcessors;
        private final List<Class<AfterBindPostProcessor>> afterProcessors;

        @SuppressWarnings("unchecked")
        private ClassContainer(Set<Class<?>> clazzSet) {
            clazzList = new ArrayList<>();
            beforeProcessors = new ArrayList<>();
            afterProcessors = new ArrayList<>();
            for (Class<?> clazz : clazzSet) {
                if (!clazz.isAnnotationPresent(Injectable.class)) {
                    continue;
                }
                if (clazz.isAssignableFrom(BeforeBindPostProcessor.class)) {
                    logger.info("Searched BeforeBindPostProcessor: {}", clazz.getName());
                    beforeProcessors.add((Class<BeforeBindPostProcessor>) clazz);
                } else if (clazz.isAssignableFrom(AfterBindPostProcessor.class)) {
                    logger.info("Searched AfterBindPostProcessor: {}", clazz.getName());
                    afterProcessors.add((Class<AfterBindPostProcessor>) clazz);
                } else {
                    logger.info("Searched injectable: {}", clazz.getName());
                    clazzList.add(clazz);
                }
            }
        }
    }

    private boolean initialized = false;
    private Binder binder;
    private ClassContainer container;

    private AutoBindExecutor(Set<Class<?>> clazzSet) {
        container = new ClassContainer(clazzSet);
    }

    protected void applyPostProcessorsBeforeInitialization() throws Exception {
        for (Class<BeforeBindPostProcessor> beforeProcessor : container.beforeProcessors) {
            BeforeBindPostProcessor beforeBindPostProcessor = beforeProcessor.getDeclaredConstructor().newInstance();
            beforeBindPostProcessor.apply(container.clazzList, binder());
            logger.info("Apply BeforeBindPostProcessor: {}", beforeProcessor.getName());
            binder.bind(beforeProcessor).toInstance(beforeBindPostProcessor);
        }
    }

    @SuppressWarnings("unchecked")
    protected void initialization() {
        for (Class<?> clazz : container.clazzList) {
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

    protected void applyPostProcessorsAfterInitialization() throws Exception {
        for (Class<AfterBindPostProcessor> afterPostProcessor : container.afterProcessors) {
            AfterBindPostProcessor afterBindPostProcessor = afterPostProcessor.getDeclaredConstructor().newInstance();
            afterBindPostProcessor.apply(container.clazzList, binder());
            logger.info("Apply AfterBindPostProcessor: {}", afterPostProcessor.getName());
            binder.bind(afterPostProcessor).toInstance(afterBindPostProcessor);
        }
    }

    @Override
    public void configure(Binder binder) {
        this.binder = binder;
        try {
            applyPostProcessorsBeforeInitialization();
            initialization();
            applyPostProcessorsAfterInitialization();
            container = null;
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            // pass
        }
    }

    public Binder binder() {
        return binder;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public static AutoBindExecutor createModule(Set<Class<?>> clazzSet) {
        return new AutoBindExecutor(clazzSet);
    }
}
