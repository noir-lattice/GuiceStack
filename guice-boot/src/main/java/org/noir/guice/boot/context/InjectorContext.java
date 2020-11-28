package org.noir.guice.boot.context;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.noir.guice.boot.executor.AutoBindExecutor;

import java.util.Set;

public class InjectorContext {
    private static Injector injector;

    public static <T> T get(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    public static boolean refresh(Set<Class<?>> clazzSet) {
        AutoBindExecutor module = AutoBindExecutor.createModule(clazzSet);
        injector = Guice.createInjector(module);
        return module.isInitialized();
    }

}
