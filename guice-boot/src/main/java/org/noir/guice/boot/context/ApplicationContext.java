package org.noir.guice.boot.context;

import org.noir.guice.boot.annotations.Injectable;
import org.noir.guice.boot.event.ApplicationCloseEvent;
import org.noir.guice.boot.event.RefreshEvent;
import org.noir.guice.boot.executor.AutoBindExecutor;
import org.noir.guice.boot.executor.ScanExecutor;
import org.noir.guice.eventbus.Event;
import org.noir.guice.eventbus.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Application context
 * 
 * 应用上下文，提供应用刷新能力
 * 
 * 在刷新过程中，应用上下文的边界在于管理扫描路径，即认为的应用相关类的框架载入
 * 捞取到相关类后，将交由injector上下文进行IoC管理
 * 
 * @see ScanExecutor#search(String)
 * @see InjectorContext#refresh(Set)
 */
public class ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Application shutdown");
            sendEvent(ApplicationCloseEvent.SHUTDOWN);
        }));
    }

    private static List<String> scanPackage;

    public static boolean refresh() {
        Predicate<Class<?>> predicate = clazz -> clazz.getAnnotation(Injectable.class) != null;
        Set<Class<?>> clazzSet = new HashSet<>();
        for (String pkg :scanPackage){
            Set<Class<?>> set = ScanExecutor.getInstance().search(pkg, predicate);
            clazzSet.addAll(set);
        }
        boolean refresh = InjectorContext.refresh(clazzSet);
        if (refresh) {
            logger.info("Application started in: {}", Thread.currentThread().getName());
            sendEvent(RefreshEvent.REFRESH_EVENT);
        } else {
            logger.info("Application start fail");
            sendEvent(ApplicationCloseEvent.START_FAIL);
        }
        return refresh;
    }

    public static void setScanPackage(List<String> scanPackage) {
        ApplicationContext.scanPackage = scanPackage;
        ApplicationContext.scanPackage.add(DEFAULT_APPLICATION_PKG);
    }

    private static void sendEvent(Event event) {
        EventPublisher eventPublisher = InjectorContext.get(EventPublisher.class);
        eventPublisher.publishEvent(event);
    }

    private static final String DEFAULT_APPLICATION_PKG = "org.noir.guice";
}
