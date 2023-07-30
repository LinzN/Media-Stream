package de.linzn.mediaStream.events;

import de.stem.stemSystem.modules.eventModule.StemEvent;

public class WiimStatusChangedEvent implements StemEvent {
    private final String oldStatus;
    private final String newStatus;

    public WiimStatusChangedEvent(String oldStatus, String newStatus) {
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public boolean isInitializeEvent() {
        return this.oldStatus == null;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }
}
