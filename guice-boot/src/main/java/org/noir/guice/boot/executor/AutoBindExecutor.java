package org.noir.guice.boot.executor;

import com.google.inject.*;
import com.google.inject.Module;

import org.noir.guice.boot.executor.binder.ClassBinder;
import org.noir.guice.boot.executor.binder.impl.AfterBindPostProcessorBinder;
import org.noir.guice.boot.executor.binder.impl.BeforeBindPostProcessorBinder;
import org.noir.guice.boot.executor.binder.impl.InstanceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Auto bind executor
 * 
 * 实现自动绑定类到container容器，同时也是guice Module实现类，用于构造Injector
 * 
 * 默认构造binder并顺序执行:
 * @see BeforeBindPostProcessorBinder
 * @see InstanceBinder
 * @see AfterBindPostProcessorBinder
 * 
 * @see org.noir.guice.boot.context.InjectorContext#refresh
 */
public class AutoBindExecutor implements Module {
    private static final Logger logger = LoggerFactory.getLogger(AutoBindExecutor.class);

    /**
     * Only create Module by factory
     * @param classSet 类集合
     * @return executor
     */
    public static AutoBindExecutor createModule(Set<Class<?>> classSet) {
        return new AutoBindExecutor(classSet);
    }

    private boolean initialized = false;
    private List<Class<?>> classes;

    private AutoBindExecutor(Set<Class<?>> classSet) {
        classes = new ArrayList<>(classSet);
    }

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void configure(Binder binder) {
        try {
            // ordering apply binder
            for (ClassBinder classBinder : BINDERS) {
                classBinder.apply(classes, binder);
            }
            initialized = true;
        } catch (Exception e) {
            logger.error("Failed to AutoBind class: ", e.getMessage());
            e.printStackTrace();
        }
    }

    private static final ClassBinder[] BINDERS = new ClassBinder[] {
        new BeforeBindPostProcessorBinder(),
        new InstanceBinder(),
        new AfterBindPostProcessorBinder(),
    };
}
