package org.noir.guice.boot.executor.binder.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Binder;

import org.noir.guice.boot.executor.binder.ClassBinder;
import org.noir.guice.boot.executor.functionals.AfterBindPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * After processor binder
 */
public class AfterBindPostProcessorBinder implements ClassBinder {
    private static final Logger logger = LoggerFactory.getLogger(AfterBindPostProcessorBinder.class);

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void apply(List<Class<?>> classes, Binder binder) {
        List<Class<?>> bindProcessorList = classes.stream().filter(this::isSupportClass).collect(Collectors.toList());
        for (Class afterProcessor : bindProcessorList) {
            try {
                AfterBindPostProcessor afterBindPostProcessor =
                        (AfterBindPostProcessor) afterProcessor.getDeclaredConstructor().newInstance();
                logger.info("Get AfterBindPostProcessor: {}", afterProcessor.getName());
                afterBindPostProcessor.apply(classes, binder);
                logger.info("Apply AfterBindPostProcessor: {}", afterProcessor.getName());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("fail to construct instance of :" + afterProcessor.getName());
            }
        }
    }

    public boolean isSupportClass(Class<?> clazz) {
        boolean isSupport = AfterBindPostProcessor.class.isAssignableFrom(clazz);
        if (isSupport) {
            logger.info("Searched AfterBindPostProcessor: {}", clazz.getName());
        }
        return isSupport;
    }

}
