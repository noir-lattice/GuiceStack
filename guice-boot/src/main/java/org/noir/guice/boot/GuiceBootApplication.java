package org.noir.guice.boot;

import com.google.common.collect.Lists;
import org.noir.guice.boot.annotations.Bootstrap;
import org.noir.guice.boot.context.ApplicationContext;
import org.noir.guice.boot.context.GracefullyCloseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class GuiceBootApplication extends GracefullyCloseContext {

    private final static Logger logger = LoggerFactory.getLogger(GuiceBootApplication.class);

    private GuiceBootApplication() {
    }

    public static void run(Class<?> clazz) {
        Bootstrap annotation = clazz.getAnnotation(Bootstrap.class);
        if (Objects.isNull(annotation)) {
            logger.error("Bootstrap application class not find annotation, please check the boot file.");
            throw new RuntimeException("Bootstrap application class not find annotation, please check the boot file.");
        } else {
            String[] scanPackage = annotation.scanPackage();
            List<String> scanPackageList = scanPackage.length > 0 ? Lists.newArrayList(scanPackage) : Lists.newArrayList(clazz.getPackageName());
            ApplicationContext.setScanPackage(scanPackageList);
            boolean refreshed = ApplicationContext.refresh();
            if (refreshed) {
                start();
            }
        }
    }
}
