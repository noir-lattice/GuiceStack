package org.noir.guice.boot.context;

import org.noir.guice.boot.executor.ScanExecutor;

import java.util.Set;

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
