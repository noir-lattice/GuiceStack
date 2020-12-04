package org.noir.guice.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.noir.guice.boot.annotations.Injectable;

/**
 * @author noir
 * @date 2020/11/30 22:12
 */
@Injectable
@SuppressWarnings("UnstableApiUsage")
public class EventPublisher {

    @Inject
    private EventBus eventBus;

    public void publishEvent(Event event) {
        eventBus.post(event);
    }
}
