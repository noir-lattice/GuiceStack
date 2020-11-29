package org.noir.guice.boot.executor.binder.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Binder;

import org.noir.guice.boot.executor.binder.ClassBinder;
import org.noir.guice.boot.executor.functionals.BeforeBindPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Before processor binder
 */
public class BeforeBindPostProcessorBinder implements ClassBinder {
    private static final Logger logger = LoggerFactory.getLogger(BeforeBindPostProcessorBinder.class);

    @Override
    public void apply(List<Class<?>> classes, Binder binder) {
        List<Class<?>> bindProcessorList = classes.stream().filter(this::isSupportClass).collect(Collectors.toList());
        for (Class<?> beforeProcessor : bindProcessorList) {
            BeforeBindPostProcessor beforeProcessorInstance = (BeforeBindPostProcessor) binder
                    .getProvider(beforeProcessor).get();
            logger.info("Get BeforeBindPostProcessor: {}", beforeProcessor.getName());
            beforeProcessorInstance.apply(classes, binder);
            logger.info("Apply BeforeBindPostProcessor: {}", beforeProcessor.getName());
        }
    }

    public boolean isSupportClass(Class<?> clazz) {
        logger.info("Searched BeforeBindPostProcessor: {}", clazz.getName());
        return clazz.isAssignableFrom(BeforeBindPostProcessor.class);
    }

}
