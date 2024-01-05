package de.linzn.mediaStream.events;

import de.stem.stemSystem.modules.eventModule.StemEvent;

public class WiimActiveEvent implements StemEvent {
    private final int mode;
    public WiimActiveEvent(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
