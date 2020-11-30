package org.noir.guice.boot.eventbus;

import java.util.UUID;

/**
 * @author noir
 */
public class Event {

    private String id;

    public String getId() {
        if (id == null) {
            synchronized (this) {
                if (id == null) {
                    id = UUID.randomUUID().toString();
                }
            }
        }
        return id;
    }

    public String getEventName() {
        return this.getClass().getSimpleName();
    }

}
