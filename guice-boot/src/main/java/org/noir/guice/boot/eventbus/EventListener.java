package org.noir.guice.boot.eventbus;

import com.google.common.eventbus.Subscribe;

/**
 * @author noir
 */
@SuppressWarnings("UnstableApiUsage")
public interface EventListener<T extends Event> {

    /**
     * Event invoker
     * @param event event instance
     */
    @Subscribe
    void subscript(T event);

}
