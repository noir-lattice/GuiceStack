package org.noir.guice.interceptor;

import java.lang.reflect.Method;

import com.google.inject.matcher.Matcher;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * Guice Interceptor interface
 * 
 * 实现拦截并交由IoC容器管理的类将被自动绑定切面
 * @see InterceptorBeforePostProcessor
 */
public interface GuiceInterceptor extends MethodInterceptor {

    /**
     * 类形匹配器
     * 
     * 仅对IoC上下文的对象有用
     * @return Matcher
     */
    Matcher<Class<?>> getClassMatcher();

    /**
     * 方法匹配起
     * 
     * @return Matcher
     */
    Matcher<? super Method> getMethodMatcher();

}
