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

package de.linzn.mediaStream.events;

import de.linzn.stem.modules.eventModule.StemEvent;

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
