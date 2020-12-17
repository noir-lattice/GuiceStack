package org.noir.guice.boot.listener;

import org.noir.guice.boot.annotations.Injectable;
import org.noir.guice.boot.context.GracefullyCloseContext;
import org.noir.guice.boot.event.ApplicationCloseEvent;
import org.noir.guice.boot.executor.binder.impl.BeforeBindPostProcessorBinder;
import org.noir.guice.eventbus.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ApplicationCloseListener
 */
@Injectable
public class ApplicationCloseListener implements EventListener<ApplicationCloseEvent> {

    private static final Logger logger = LoggerFactory.getLogger(BeforeBindPostProcessorBinder.class);

    @Override
    public void subscript(ApplicationCloseEvent event) {
        GracefullyCloseContext.close();
        logger.info("Closed Application main thread: {}", GracefullyCloseContext.getMainThread().getName());
    }

}
