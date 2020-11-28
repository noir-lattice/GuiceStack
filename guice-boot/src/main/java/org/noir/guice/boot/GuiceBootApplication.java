package org.noir.guice.boot;

import org.noir.guice.boot.annotations.Bootstrap;
import org.noir.guice.boot.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.locks.LockSupport;

public class GuiceBootApplication {

    private final static Logger logger = LoggerFactory.getLogger(GuiceBootApplication.class);

    private GuiceBootApplication() {
    }

    public static void run(Class<?> clazz) {
        Bootstrap annotation = clazz.getAnnotation(Bootstrap.class);
        if (Objects.isNull(annotation)) {
            logger.error("Bootstrap application class not find annotation, please check the boot file.");
        } else {
            String scanPackage = annotation.scanPackage();
            scanPackage = "".equalsIgnoreCase(scanPackage) ? clazz.getPackageName() : scanPackage;
            ApplicationContext.setScanPackage(scanPackage);
            boolean refreshed = ApplicationContext.refresh();
            if (refreshed) {
                LockSupport.park();
            }
        }
    }
}
