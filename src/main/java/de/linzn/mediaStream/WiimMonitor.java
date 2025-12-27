/*
 * Copyright (c) 2025 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.mediaStream;

import de.linzn.mediaStream.events.WiimActiveEvent;
import de.linzn.mediaStream.events.WiimApiErrorEvent;
import de.linzn.mediaStream.events.WiimStandbyEvent;
import de.linzn.mediaStream.events.WiimStatusChangedEvent;
import de.linzn.stem.STEMApp;

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
        if (this.mediaManager.getWiimAPI().hasAPIError()) {
            STEMApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimApiErrorEvent());
        } else {
            boolean isStandby = this.mediaManager.getWiimAPI().getWiimPlayer().isStandby();

            if (this.isStandby == null || this.isStandby.get() != isStandby) {
                this.isStandby = new AtomicBoolean(isStandby);
                if (this.isStandby.get()) {
                    STEMApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimStandbyEvent());
                } else {
                    int mode = this.mediaManager.getWiimAPI().getWiimPlayer().get_mode();
                    STEMApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimActiveEvent(mode));
                }
            }

            String newStatus = this.mediaManager.getWiimAPI().getWiimPlayer().get_status();
            int newMode = this.mediaManager.getWiimAPI().getWiimPlayer().get_mode();


            if (!newStatus.equalsIgnoreCase(status) || newMode != mode) {
                STEMApp.getInstance().getEventModule().getStemEventBus().fireEvent(new WiimStatusChangedEvent(status, newStatus, newMode));
                this.status = newStatus;
                this.mode = newMode;
            }
        }
    }
}
