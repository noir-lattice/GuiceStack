package org.noir.guice.interceptor;

import java.util.Collection;

import com.google.inject.Binder;

import org.noir.guice.boot.annotations.Injectable;
import org.noir.guice.boot.executor.functionals.BeforeBindPostProcessor;

/**
 * Interceptor BeforePostProcessor
 * 
 * 提供拦截器对象的自动注册，对于所有在IoC管理下的
 * 且实现了GuiceInterceptor对象提供自动绑定拦截的能力
 * 
 * @see GuiceInterceptor
 */
@Injectable
public class InterceptorBeforePostProcessor implements BeforeBindPostProcessor {

    @Override
    public void apply(Collection<Class<?>> clazzSet, Binder binder) {
        for (Class<?> clazz : clazzSet) {
            if (isInterceptor(clazz)) {
                GuiceInterceptor interceptor = (GuiceInterceptor) binder.getProvider(clazz).get();
                binder.bindInterceptor(interceptor.getClassMatcher(), interceptor.getMethodMatcher(), interceptor);
            }
        }
    }

    private boolean isInterceptor(Class<?> clazz) {
        return clazz.isAssignableFrom(GuiceInterceptor.class);
    }

}
