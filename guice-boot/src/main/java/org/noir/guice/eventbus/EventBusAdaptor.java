package org.noir.guice.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.inject.Binder;
import org.noir.guice.boot.annotations.Injectable;
import org.noir.guice.boot.executor.functionals.AfterBindPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @author noir
 */
@Injectable
@SuppressWarnings("UnstableApiUsage")
public class EventBusAdaptor implements AfterBindPostProcessor {

    private final static Logger logger = LoggerFactory.getLogger(EventBusAdaptor.class);

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
        boolean isSupport = EventListener.class.isAssignableFrom(clazz);
        if (isSupport) {
            logger.info("Register event listener: " + clazz.getName());
        }
        return isSupport;
    }

}
