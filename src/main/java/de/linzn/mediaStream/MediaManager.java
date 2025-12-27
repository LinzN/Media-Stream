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

import de.linzn.stem.STEMApp;
import de.linzn.wiimJavaApi.WiimAPI;

import java.util.concurrent.TimeUnit;

public class MediaManager {
    private String ipAddress;
    private WiimAPI wiimAPI;

    public MediaManager() {
        this.ipAddress = MediaStreamPlugin.mediaStreamPlugin.getDefaultConfig().getString("wiimDevice.ipAddress", "10.50.0.99");
        this.wiimAPI = new WiimAPI(this.ipAddress);
        this.wiimAPI.setWiimLogger(new WiimCustomLogger());
        this.wiimAPI.setSslCheck(false);
        this.wiimAPI.setPullInterval(500, TimeUnit.MILLISECONDS);
        this.wiimAPI.connect();
        STEMApp.getInstance().getScheduler().runRepeatScheduler(MediaStreamPlugin.mediaStreamPlugin, new WiimMonitor(this), 20, 1, TimeUnit.SECONDS);
    }

    public WiimAPI getWiimAPI() {
        return this.wiimAPI;
    }
}
