package org.noir.guice.boot.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.inject.Binder;
import org.noir.guice.boot.annotations.Injectable;
import org.noir.guice.boot.executor.functionals.AfterBindPostProcessor;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author noir
 */
@Injectable
@SuppressWarnings("UnstableApiUsage")
public class EventBusAdaptor implements AfterBindPostProcessor {
    private EventBus eventBus;

    @Override
    public void apply(Collection<Class<?>> clazzSet, Binder binder) {
        bindEventBusInstance(binder);
        clazzSet.stream().filter(this::isSupport).forEach(eventBus::register);
    }

    private void bindEventBusInstance(Binder binder) {
        eventBus = new EventBus();
        binder.bind(EventBus.class).toInstance(eventBus);
    }

    private boolean isSupport(Class<?> clazz) {
        return clazz.isAssignableFrom(EventListener.class);
    }

}
