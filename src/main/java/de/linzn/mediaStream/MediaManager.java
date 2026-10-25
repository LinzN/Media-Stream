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

import de.linzn.stem.modules.pluginModule.STEMPlugin;
import de.linzn.wiim.api.WiiMClient;

public class MediaManager {

    private final WiiMClient client;
    private final MediaListener mediaListener;

    public MediaManager(STEMPlugin plugin) {
        String ipAddress = plugin.getDefaultConfig().getString("wiimDevice.ipAddress", "10.50.0.99");
        int pollInterval = Math.max(200, plugin.getDefaultConfig().getInt("wiimDevice.pollIntervalMs", 600));
        this.client = new WiiMClient(ipAddress);
        this.client.registerLogger(new WiiMStemLogger(plugin));
        this.mediaListener = new MediaListener(plugin, client);
        this.mediaListener.start(pollInterval);
    }

    /**
     * Stops polling the WiiM device.
     */
    public void shutdown() {
        this.mediaListener.stop();
    }

    public WiiMClient getClient() {
        return client;
    }
}
