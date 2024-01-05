package de.linzn.mediaStream;

import de.linzn.mediaStream.events.WiimActiveEvent;
import de.linzn.mediaStream.events.WiimApiErrorEvent;
import de.linzn.mediaStream.events.WiimStandbyEvent;
import de.linzn.mediaStream.events.WiimStatusChangedEvent;
import de.stem.stemSystem.STEMSystemApp;

import java.util.concurrent.atomic.AtomicBoolean;

public class WiimMonitor implements Runnable {

    private MediaManager mediaManager;

    private String status = null;

    private int mode = -1;

    private AtomicBoolean isStandby = null;

    public WiimMonitor(MediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    @Override
    public void run() {
        if(this.mediaManager.getWiimAPI().hasAPIError()){
            STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimApiErrorEvent());
        } else {
            boolean isStandby = this.mediaManager.getWiimAPI().getWiimPlayer().isStandby();

            if (this.isStandby == null || this.isStandby.get() != isStandby) {
                this.isStandby = new AtomicBoolean(isStandby);
                if (this.isStandby.get()) {
                    STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimStandbyEvent());
                } else {
                    int mode = this.mediaManager.getWiimAPI().getWiimPlayer().get_mode();
                    STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimActiveEvent(mode));
                }
            }

            String newStatus = this.mediaManager.getWiimAPI().getWiimPlayer().get_status();
            int newMode = this.mediaManager.getWiimAPI().getWiimPlayer().get_mode();


            if (!newStatus.equalsIgnoreCase(status) || newMode != mode) {
                STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimStatusChangedEvent(status, newStatus, newMode));
                this.status = newStatus;
                this.mode = newMode;
            }
        }
    }
}
