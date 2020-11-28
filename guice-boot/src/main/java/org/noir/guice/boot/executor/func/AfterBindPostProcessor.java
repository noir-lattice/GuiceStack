package org.noir.guice.boot.executor.func;

import com.google.inject.Binder;

import java.util.Collection;

@FunctionalInterface
public interface AfterBindPostProcessor {

    void apply(Collection<Class<?>> clazzSet, Binder binder);

}
