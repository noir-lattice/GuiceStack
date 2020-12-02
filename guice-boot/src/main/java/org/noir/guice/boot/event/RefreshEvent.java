package org.noir.guice.boot.event;

import org.noir.guice.eventbus.Event;

/**
 * Refresh event
 */
public class RefreshEvent extends Event {

    private RefreshEvent() {
    }

    public static final RefreshEvent REFRESH_EVENT = new RefreshEvent();
}
