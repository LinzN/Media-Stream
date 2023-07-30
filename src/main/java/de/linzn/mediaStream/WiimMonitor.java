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
    //private Date lastStatusSwitch = null;
    //private long msToStandby = 1000L * 60 * 3;

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
                    STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimActiveEvent());
                }
            }

            String newStatus = this.mediaManager.getWiimAPI().getWiimPlayer().get_status();

            if (!newStatus.equalsIgnoreCase(status)) {
                STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimStatusChangedEvent(status, newStatus));
                this.status = newStatus;
            }
        }
    }
/*
    @Override
    public void run() {
        String newStatus = this.mediaManager.getWiimAPI().getWiimPlayer().get_status();
        boolean isInitializing = false;
        if (this.status == null) {
            isInitializing = true;
        }

        if (!newStatus.equalsIgnoreCase(status)) {
            STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimStatusChangedEvent(status, newStatus));
            this.status = newStatus;
            this.lastStatusSwitch = new Date();
            if (!isInitializing && this.isStandby.get() && this.status.equalsIgnoreCase("play")) {
                STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimActiveEvent());
            }
            this.isStandby = new AtomicBoolean(false);
        }

        if (isInitializing) {
            STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimInitializeEvent(newStatus));
            if (this.status.equalsIgnoreCase("Stop")) {
                this.isStandby = new AtomicBoolean(true);
                STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimStandbyEvent());
            } else if (this.status.equalsIgnoreCase("Play")) {
                this.isStandby = new AtomicBoolean(false);
                STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimActiveEvent());
            }
        }

        if (this.status.equalsIgnoreCase("stop")) {
            if (!this.isStandby.get() && this.lastStatusSwitch.getTime() + msToStandby < new Date().getTime()) {
                this.isStandby.set(true);
                STEMSystemApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimStandbyEvent());
            }
        }
    }
*/
}
