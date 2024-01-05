package de.linzn.mediaStream.events;

import de.stem.stemSystem.modules.eventModule.StemEvent;

public class WiimStatusChangedEvent implements StemEvent {
    private final String oldStatus;
    private final String newStatus;
    private final int mode;

    public WiimStatusChangedEvent(String oldStatus, String newStatus, int mode) {
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.mode = mode;
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

    public int getMode() {
        return mode;
    }
}
