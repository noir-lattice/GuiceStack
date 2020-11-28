package org.noir.guice.boot.context;

import org.noir.guice.boot.executor.ScanExecutor;

import java.util.Set;

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

    private static String scanPackage;

    public static boolean refresh() {
        Set<Class<?>> clazzSet = ScanExecutor.getInstance().search(scanPackage);
        return InjectorContext.refresh(clazzSet);
    }

    public static void setScanPackage(String scanPackage) {
        ApplicationContext.scanPackage = scanPackage;
    }
}
