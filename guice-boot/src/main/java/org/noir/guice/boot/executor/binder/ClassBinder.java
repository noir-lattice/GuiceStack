package org.noir.guice.boot.executor.binder;

import java.util.List;

import com.google.inject.Binder;

/**
 * Class binder
 * 
 * 该类仅仅实将逻辑分类，由框架自动实例化调用后释放，不注入Guice容器提供扩展
 */
public interface ClassBinder {

    void apply(List<Class<?>> classes, Binder binder);

}
