package org.noir.guice.boot.context;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.noir.guice.boot.executor.AutoBindExecutor;

import java.util.Set;

/**
 * Injector context
 * 
 * 实例注册者上下文，提供全局的IoC容器获取，并提供injector实例的刷新
 * 需要注意的是，injector实例的刷新并不是公有的，这是只提供给应用上下
 * 文拥有刷新injector的能力 {@code ApplicationContext#refresh}
 * 
 * injector ctx的能力在管理与Guice框架的结合，即module管理与inject
 * 具柄的再封装，自动绑定的能力将交由AutoBindExecutor即相关的Binder
 * 
 * @see AutoBindExecutor#configure(com.google.inject.Binder)
 */
public class InjectorContext {
    private static Injector injector;

    public static <T> T get(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    static boolean refresh(Set<Class<?>> clazzSet) {
        AutoBindExecutor module = AutoBindExecutor.createModule(clazzSet);
        injector = Guice.createInjector(module);
        return module.isInitialized();
    }

}
