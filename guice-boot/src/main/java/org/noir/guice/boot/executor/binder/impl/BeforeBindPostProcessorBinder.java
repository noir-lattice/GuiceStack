package org.noir.guice.boot.executor.binder.impl;

import java.lang.reflect.InvocationTargetException;
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
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void apply(List<Class<?>> classes, Binder binder) {
        List<Class<?>> bindProcessorList = classes.stream().filter(this::isSupportClass).collect(Collectors.toList());
        for (Class beforeProcessor : bindProcessorList) {
            try {
                BeforeBindPostProcessor beforeBindPostProcessor
                        = (BeforeBindPostProcessor) beforeProcessor.getDeclaredConstructor().newInstance();
                logger.info("Get BeforeBindPostProcessor: {}", beforeProcessor.getName());
                binder.bind(beforeProcessor).toInstance(beforeBindPostProcessor);
                beforeBindPostProcessor.apply(classes, binder);
                logger.info("Apply BeforeBindPostProcessor: {}", beforeProcessor.getName());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("fail to construct instance of :" + beforeProcessor.getName());
            }
        }
    }

    public boolean isSupportClass(Class<?> clazz) {
        boolean isSupport = BeforeBindPostProcessor.class.isAssignableFrom(clazz);
        if (isSupport) {
            logger.info("Searched BeforeBindPostProcessor: {}", clazz.getName());
        }
        return isSupport;
    }

}
